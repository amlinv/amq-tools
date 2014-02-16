amq-tools
=========

Set of tools with a convenient command-line interface for performing operations with ActiveMQ, such as consuming and
producing messages.


====================
Supported Operations
====================

* Produce text messages to a Queue or Topic with one-line-per-message input from standard input.
* Consume messages from a Queue or Topic and display on the console.
* Throw an exception in a transaction after consuming a message.



==============
Design Concept
==============

* Simple tools for quick and easy operation.
* Inversion of control coding practices to minimize redundant code and increase flexibility in the toolset.
