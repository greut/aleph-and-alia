# Aleph and Alia

## Prepare the db

```console
% python -m venv venv
% . venv/bin/activate
(venv)% pip install -U pip
(venv)% pip install ccm
(venv)% ccm create test -v 3.11.3 -n 1 # only once
(venv)% ccm start

(venv)% ccm node1 cqlsh
cqlsh> create keyspace if not exists foo with replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};
cqlsh> create table if not exists foo.bar (id uuid primary key);
cqlsh> insert into foo.bar (id) values (ae463480-7032-42c8-a4e5-0631e20c0519);
cqlsh> quit
```

## Run it

Run the application

```console
% lein run

[main] INFO  aleph-and-alia.main - server started
```

Then query the page.

```console
% curl http://localhost:10000
```

And observe.

```
13:50:26.719 [manifold-pool-2-1] WARN  aleph-and-alia.main - status 200
13:50:26.739 [manifold-pool-1-1] WARN  aleph-and-alia.main - status d/chain 200
13:50:26.753 [manifold-pool-1-2] WARN  aleph-and-alia.main - status d/let-flow 200
13:50:26.757 [manifold-pool-2-1] WARN  aleph-and-alia.main - alia.execute ({:id #uuid "ae463480-7032-42c8-a4e5-0631e20c0519"})
13:50:26.760 [manifold-pool-2-3] WARN  aleph-and-alia.main - alia.manifold.execute d/chain ({:id #uuid "ae463480-7032-42c8-a4e5-0631e20c0519"})
13:50:26.768 [cluster1-nio-worker-3] WARN  aleph-and-alia.main - alia.manifold.execute d/let-flow ({:id #uuid "ae463480-7032-42c8-a4e5-0631e20c0519"})
```

All the _warns_ should be ran on a `manifold-pool` thread and none of them should stay on the Cassandra worker ones.
