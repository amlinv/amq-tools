license notice
==============
    Licensed to the Apache Software Foundation (ASF) under one or more
    contributor license agreements.  See the NOTICE file distributed with
    this work for additional information regarding copyright ownership.
    The ASF licenses this file to You under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with
    the License.  You may obtain a copy of the License at
   
    http://www.apache.org/licenses/LICENSE-2.0
   
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


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


========
WEB SITE
========

All documentation currently resides on github; see the wiki (here: https://github.com/amlinv/amq-tools/wiki/AMQ-TOOLS-Home) for the available documentation.
