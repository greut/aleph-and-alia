(ns aleph-and-alia.main
    (:require [aleph.http :as http]
              [aleph.netty :as netty]
              [manifold.deferred :as d]
              [qbits.alia :as alia]
              [qbits.alia.manifold :as aliam]
              [clojure.tools.logging :refer [info warn]]))

(def url "https://www.exoscale.com/robots.txt")
(def query "select id from foo.bar limit 1;")

(defn handler
  [session req]

  ;; http
  (let [resp @(http/get url)]
    (warn "status" (:status resp)))

  @(d/chain (http/get url)
           :status
           #(warn "status d/chain" %))

  @(d/let-flow [resp (http/get url)]
    (warn "status d/let-flow" (:status resp)))

  ;; cassandra
  (let [bars (alia/execute session query)]
    (warn "alia.execute" bars))

  @(d/chain (aliam/execute session query)
            #(warn "alia.manifold.execute d/chain" %))

  @(d/let-flow [bars (aliam/execute session query)]
    (warn "alia.manifold.execute d/let-flow" bars))

  {:status 200
   :headers {"content-type" "text/plain"}
   :body "hello world!"})

(defn -main
  [& _]
  (let [cluster (alia/cluster {:contact-points ["localhost"]})
        session (alia/connect cluster)
        server  (http/start-server (partial handler session)
                                   {:port 10000})]
    (info "server started")
    (netty/wait-for-close server)))
