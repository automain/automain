/*
 * Copyright 2018 fastjdbc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.automain.service;

import com.github.automain.bean.Test;
import com.github.automain.dao.TestDao;
import com.github.automain.vo.TestVO;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

public class TestService extends BaseService<Test, TestDao> {

    public TestService(Test bean, TestDao dao) {
        super(bean, dao);
    }

    public PageBean<Test> selectTableForCustomPage(ConnectionBean connection, TestVO bean) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean);
    }

}