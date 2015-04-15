amit-nmap-toy-project
=====================
Toy Project to scan ports open on a host using nmap

Moving Target
==============
Finished and manually tested the api endpoints, but still working on front end using Jquery.

There's 3 main api endpoints - 1 post and 2 gets.  All of them are swagger annotated with information on how to use them

TBD- Push Jquery changes later.

How To run
=============
This is an sbt project and uses play 2 framework.

1)
Clone this project

2)
Install mysql and create a database for this project like so (it can be whatever db name you want though)

create database nmap_toy_project character set utf8 collate utf8_bin;

3)
Open conf/application.conf and edit following 3 entries to match your database and credentials

db.default.url

db.default.user

db.default.pass

4)
Run the application

Goto root of the project on command/terminal and run following

On Windows - activator run

On linux/mac - ./activator run

This will start on default port 9000.  However, you can change the port like so if needed

On Windows - activator "run 9002"

On linux/mac - ./activator "run 9002"

5)
Once application is up, refresh your database and make sure all tables are created automatically - this should work.  I've tested it.

All database schema evolutions are in conf/evolutions/default

6)
open browser and hit following

http://localhost:9000/assets/docs/index.html

and then click on "List Operations"

7)
call the /nmap POST operation to start scanning a host.  This end point accepts only 1 host at a time.

I'll be splitting the hosts from front end using jquery and calling each separately later

Specify the json POST payload like so, where www.google.com is the host to scan

{"hostName": "www.google.com"}

This call will return a transactionId in response.

Use this transactionId and now call the GET /nmap/{transactionId} end point to check the progress of the scan.

Call the GET every few seconds, and you should get a complete status when the scan is done.

So now you can use these 2 endpoints to generate a certain history

Then use /history/{host} - host or ip, to get all historic information about this host.

8)

To import in IDE, import this as an sbt project.

