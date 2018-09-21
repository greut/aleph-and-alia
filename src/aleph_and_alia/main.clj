(ns aleph-and-alia.main
    (:require [aleph.http :as http]
              [aleph.netty :as netty]
              [manifold.deferred :as d]
              [qbits.alia :as alia]
              [qbits.alia.manifold :as aliam]
              [clojure.tools.logging :refer [info warn]]))

(def query "select id from foo.bar limit 1;")

(defn handler
  [session req]
  (let [bars (alia/execute session query)]
    (warn "alia.execute" bars))

  (d/chain (aliam/execute session query)
           (fn [bars]
               (warn "alia.manifold.execute" bars)))
  (d/let-flow [bars (aliam/execute session query)]
    (warn "alia.manifold.execute + let-flow" bars))

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
