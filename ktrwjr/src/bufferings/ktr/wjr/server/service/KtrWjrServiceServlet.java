/*
 * Copyright 2010 bufferings[at]gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package bufferings.ktr.wjr.server.service;

import java.util.List;
import java.util.Map;

import bufferings.ktr.wjr.client.service.KtrWjrService;
import bufferings.ktr.wjr.shared.model.WjrConfig;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * A KtrWjrService servlet for running without Slim3.
 * <p>
 * This servlet delegates all operations to {@link KtrWjrServiceImpl}.
 * </p>
 * 
 * @author bufferings[at]gmail.com
 */
public class KtrWjrServiceServlet extends RemoteServiceServlet implements
    KtrWjrService {

  private static final long serialVersionUID = 1L;

  /**
   * delegation
   */
  protected KtrWjrServiceImpl delegate = new KtrWjrServiceImpl();

  /**
   * {@inheritDoc}
   */
  public WjrConfig loadConfig(Map<String, List<String>> parameterMap) {
    return delegate.loadConfig(parameterMap);
  }
  
  /**
   * {@inheritDoc}
   */
  public WjrStore loadStore(Map<String, List<String>> parameterMap) {
    return delegate.loadStore(parameterMap);
  }

  /**
   * {@inheritDoc}
   */
  public WjrMethodItem runTest(WjrMethodItem methodItem,
      Map<String, List<String>> parameterMap) {
    return delegate.runTest(methodItem, parameterMap);
  }

}
