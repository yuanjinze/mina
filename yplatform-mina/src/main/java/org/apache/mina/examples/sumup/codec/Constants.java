/*
 *   @(#) $Id: Constants.java 209237 2005-07-05 07:44:16Z trustin $
 *
 *   Copyright 2004 The Apache Software Foundation
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.apache.mina.examples.sumup.codec;

/**
 * Provides SumUp protocol constants.
 * 
 * @author The Apache Directory Project
 * @version $Rev: 209237 $, $Date: 2005-07-05 15:44:16 +0800 (Tue, 05 Jul 2005) $
 */
public class Constants
{
    public static final int TYPE_LEN = 2;
    public static final int SEQUENCE_LEN = 4;
    public static final int HEADER_LEN = TYPE_LEN + SEQUENCE_LEN;
    public static final int BODY_LEN = 12;
    public static final int RESULT = 0;
    public static final int ADD = 1;
    public static final int RESULT_CODE_LEN = 2;
    public static final int RESULT_VALUE_LEN = 4;
    public static final int ADD_BODY_LEN = 4;
    public static final int RESULT_OK = 0;
    public static final int RESULT_ERROR = 1;

    private Constants()
    {
    }
}
