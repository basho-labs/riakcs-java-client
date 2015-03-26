/*
 * Copyright (c) 2012 Basho Technologies, Inc. All Rights Reserved. This file is provided to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.basho.riakcs.client.api;

/**
 * All exceptions are converted to RuntimeException to reduce checked exceptions
 */
public class RiakCSException extends RuntimeException {

	private static final long serialVersionUID = 3652091041677170597L;

	/**
	 * Wrap a checked {@link Exception} into a RuntimeException keeping the stack trace in tact
	 * 
	 * @param e
	 *            original {@link Exception}
	 */
	public RiakCSException(Exception e) {
		super(e);
	}

	/**
	 * Create a new RuntimeException with the passed string error message
	 * 
	 * @param errorMessage
	 *            String message to set
	 */
	public RiakCSException(String errorMessage) {
		super(errorMessage);
	}

}
