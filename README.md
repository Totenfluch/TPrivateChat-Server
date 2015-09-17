# TPrivateChat-Server
Server for "TPrivateChat-Client"

* Server for: https://github.com/Totenfluch/TPrivateChat-Client

To compile you need: commons-codec-1.8

The Server is able to run on a custom Port with:

-java -jar <jarname>.jar <port>


With this Server you are not able to read cleantext Messages that
the Users use to communicate with each other

It is designed to only repeat the encrypted messages to all connected users
(Actually only to those in the channel)

commands to the Server (switch the channel, switch the display name, clean chat)
are not encrypted and those are considered a known security issue that will be taken care of later on



