/*
 * Copyright 2004-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.orm.hibernate

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.hibernate.FlushMode
import org.springframework.orm.hibernate3.HibernateTransactionManager
import org.springframework.transaction.TransactionDefinition

/**
 * Extends the standard class to always set the flush mode to manual when in a read-only transaction.
 *
 * @author Burt Beckwith
 */
@CompileStatic
class GrailsHibernateTransactionManager extends HibernateTransactionManager {

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        super.doBegin transaction, definition

        if (definition.isReadOnly()) {
            // transaction is HibernateTransactionManager.HibernateTransactionObject private class instance
            // always set to manual; the base class doesn't because the OSIVI has already registered a session
            setFlushModeManual(transaction)
        }
    }

    @CompileDynamic
    protected void setFlushModeManual(transaction) {
        transaction.sessionHolder?.session?.flushMode = FlushMode.MANUAL
    }
}
