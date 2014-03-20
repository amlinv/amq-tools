/**
 *    Licensed to the Apache Software Foundation (ASF) under one or more
 *    contributor license agreements.  See the NOTICE file distributed with
 *    this work for additional information regarding copyright ownership.
 *    The ASF licenses this file to You under the Apache License, Version 2.0
 *    (the "License"); you may not use this file except in compliance with
 *    the License.  You may obtain a copy of the License at
 *   
 *    http://www.apache.org/licenses/LICENSE-2.0
 *   
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.amlinv.mbus.util.templ;

public interface TimeoutStrategy {
	/**
	 * Determine whether timeouts are enabled.  Called at the start of each operation.
	 *
	 * @return	true => timeouts are enabled; false => timeouts are disabled (the other methods will be
	 *		ignored).
	 */
	boolean	isTimeoutEnabled();

	/**
	 * Determine the length of the timeout.  Called at the start of each operation for which timeouts are enabled.
	 *
	 * @return	timeout - the number of milliseconds to wait before timing out the operation.
	 */
	long	getTimeout();

	/**
	 * Defines whether the operation should terminate the processor after timeout; called each time a timeout
	 * occurs, so dynamic results are possible (e.g. giving up only after a fixed number of timeouts).
	 *
 	 * @return	true => if the timeout should terminate the processor; false => if the processor should
	 *		continue in spite of the timeout.
 	 */
	boolean shouldTerminate();
}
