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
09:35:10.298 [manifold-pool-2-1] WARN  aleph-and-alia.main - alia.execute ({:id #uuid "ae463480-7032-42c8-a4e5-0631e20c0519"})
09:35:10.308 [manifold-pool-2-2] WARN  aleph-and-alia.main - alia.manifold.execute ({:id #uuid "ae463480-7032-42c8-a4e5-0631e20c0519"})
09:35:10.315 [cluster1-nio-worker-2] WARN  aleph-and-alia.main - alia.manifold.execute + let-flow ({:id #uuid "ae463480-7032-42c8-a4e5-0631e20c0519"})
```

The three _warns_ should be ran on a `manifold-pool` thread and none of them should stay on the Cassandra worker ones.
