package org.apache.maven.surefire.junitcore;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.surefire.api.report.ReporterFactory;

import java.util.Map;

/**
 * @author Kristian Rosenvold
 */
final class MethodsParallelRunListener
    extends ConcurrentRunListener
{
    private volatile TestSet lastStarted;

    private final Object lock = new Object();

    MethodsParallelRunListener( Map<String, TestSet> classMethodCounts, ReporterFactory reporterFactory,
                                boolean reportImmediately )
    {
        super( reporterFactory, reportImmediately, classMethodCounts );
    }

    @Override
    protected void checkIfTestSetCanBeReported( TestSet testSetForTest )
    {
        synchronized ( lock )
        {
            if ( testSetForTest != lastStarted )
            {
                if ( lastStarted != null )
                {
                    lastStarted.setAllScheduled( getRunListener() );
                }
                lastStarted = testSetForTest;
            }
        }
    }
}
