/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.oap.server.core.analysis.worker;

import org.apache.skywalking.oap.server.core.analysis.indicator.*;
import org.apache.skywalking.oap.server.core.exporter.*;
import org.apache.skywalking.oap.server.core.worker.AbstractWorker;
import org.apache.skywalking.oap.server.library.module.ModuleManager;

/**
 * @author wusheng
 */
public class ExportWorker extends AbstractWorker<Indicator> {
    private ModuleManager moduleManager;
    private MetricValuesExportService exportService;

    public ExportWorker(int workerId, ModuleManager moduleManager) {
        super(workerId);
        this.moduleManager = moduleManager;
    }

    @Override public void in(Indicator indicator) {
        if (exportService != null || moduleManager.has(ExporterModule.NAME)) {
            if (indicator instanceof WithMetadata) {
                if (exportService == null) {
                    exportService = moduleManager.find(ExporterModule.NAME).provider().getService(MetricValuesExportService.class);
                }
                exportService.export(((WithMetadata)indicator).getMeta(), indicator);
            }
        }
    }
}
