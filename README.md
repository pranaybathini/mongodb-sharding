For setting up the sharded cluster and configuring it through docker, follow the below link. 

Link : [MongoDB sharded cluster configuration](https://medium.com/@pranaygoud36/mongodb-sharding-9c5357a95ec1)

To set up locally without docker, follow the below steps.

### Requirements

MongoDB

### Installation

1. Download the tar ball. Here I am giving the link for Ubuntu 18.04 Linux x64. Download according to your requirements from here [Download mongoDB](https://www.mongodb.com/download-center/community?tck=docs_server)

> https://repo.mongodb.org/apt/ubuntu/dists/bionic/mongodb-org/4.2/multiverse/binary-amd64/mongodb-org-server_4.2.6_amd64.deb

2. Extract the files from the downloaded archive

> tar -zxvf mongodb-linux-*-4.2.6.tgz

3. Create the data and log directories.

      a. Create a directory where the MongoDB instance stores its data

    > sudo mkdir -p /var/lib/mongo

      b. Create a directory where the MongoDB instance stores its log

    > sudo mkdir -p /var/log/mongodb.

      c. The user that starts the MongoDB process must have read and write permission to these directories. For example, if you intend to run MongoDB as yourself:
      
    > sudo chown `whoami` /var/lib/mongo     # Or substitute another user
    
    > sudo chown `whoami` /var/log/mongodb   # Or substitute another user
    
4. Run MongoDB. Go to its bin folder under mongodb installation.

  > ./mongod --dbpath /var/lib/mongo --logpath /var/log/mongodb/mongod.log --fork
  
  For Verification.
  
  > ./mongo
  
  
  

### Setting up config servers

`sudo ./mongod --configsvr --replSet creplicaset --dbpath /data/configdb --port 27019 --bind_ip localhost`

Then connect to it mongo shell and initiate the replicaSet.

`rs.initiate({_id: "creplicaset",
    configsvr: true,
    members: [{
        _id : 0,
        host : "localhost:27019" } ]
        })`
        
### Setting up shard servers

1. Shard server 1

`sudo ./mongod --shardsvr --replSet rs1 --dbpath /data/db --port 27018 --bind_ip localhost `

Connect to its mongo shell.

`rs.initiate({
    _id :"rs1",
    members: [{ 
    _id : 0,
    host :"localhost:27018"}]
    })`
    
2. Shard server 2

`sudo ./mongod --shardsvr --replSet rs2 --dbpath /data/db1 --port 27020 --bind_ip localhost`

Connect to its mongo shell.

`rs.initiate({
    _id :"rs2",
    members: [{ 
    _id : 0,
    host :"localhost:27020"}]
    })`
        
### Start mongos query router and connect with config server

`sudo ./mongos --configdb creplicaset/localhost:27019 --bind_ip localhost`

Connect to its mongo shell.

`sh.addShard("rs1/localhost:27018")`

`sh.addShard("rs2/localhost:27020")`

### Sharding

Here people is my database and persons is my collection. You have to enable sharding on the database and specify shard key for the collection for which sharding should be enabled.

`mongos> sh.enableSharding("people")`

`mongos> sh.shardCollection("people.persons",{"personId":"hashed"})`


The above set up is one config router as a replicaset, one mongos query router and two shards(with one node replicaset). You can increase the nodes. Just assign different ports.



![happy sharding](https://fontmeme.com/temporary/70897ad4e9b1c7e726e2f7d410ac3b03.png)







        
  
