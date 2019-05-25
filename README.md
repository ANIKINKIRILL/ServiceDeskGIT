# ServiceDeskGIT
Приложение ServiceDesk
IAS SCHEMA : ias$db OcxSvhSD
MOBILE SCHEMA : mobile$db   qtRbhKga

После релиза2 дата 25.05.2019

SERVICEDESK_MOBILE_TEST PACKAGE PROCEDURES & FUNCTIONS

CREATE OR REPLACE PACKAGE BODY IAS$DB.SERVICEDESK_MOBILE_TEST AS

  PROCEDURE get_employee_requests(p_emp_id IN INTEGER /* id исполнителя в ias$db.users  */,
   p_page_number IN INTEGER /* номер страницы с заявками  */,
   p_status_id IN INTEGER /* статус заявок */)
  AS
  /***************

    Получить заявки на испонителя

  ****************/

  v_requests_page_amount CONSTANT INTEGER := 8;     -- количество запросов на одной странице
  v_p_start INTEGER := 1;       -- начальная страница
  v_p_end INTEGER := 8;     -- последняя страница
  v_emp_requests_amount INTEGER; -- количество запросов у исполнителя
  v_emp_max_page INTEGER;   -- самая последняя возможная страница
  v_employee_request_row TECH_CENTER$DB.REQUEST%ROWTYPE; -- tuple заявки с tech_center$db.request
  v_request_info TECH_CENTER$DB.REQUEST_WORKS.INFO%TYPE; -- текст заявки
  v_request_status_name VARCHAR2(30); -- название статуса заявки
  v_request_color VARCHAR2(6); -- цвет статуса заявки
  v_request_descr VARCHAR2(75); -- описание статуса заявки
  v_request_img VARCHAR2(100); -- картинка статуса заявки
  v_request_type_name VARCHAR2(100); -- тип заявки
  v_request_building_kfu_name VARCHAR2(100); -- адрес филиала КФУ, НЕ ПОДРАЗДЕЛЕНИЕ
  v_emp_request_building_kfu_id TECH_CENTER$DB.BUILDING_KFU.ID%TYPE; -- id здания кфу
  p_request_id TECH_CENTER$DB.REQUEST_EMPLOYEES.REQUEST_ID%TYPE;
  v_counter INTEGER := 1; -- счетчик, чтобы понять ставить запятую или не ствить ее после } в списке
  v_range INTEGER := 1;

  --CURSOR cur_emp_sorted_request_id IS
  --SELECT REQUEST_ID FROM TECH_CENTER$DB.REQUEST_EMPLOYEES WHERE EMPLOYEE_ID = p_emp_id ORDER BY REQUEST_ID DESC;

  CURSOR cur_emp_sorted_request_id IS
  SELECT REQUEST_ID FROM TECH_CENTER$DB.REQUEST_EMPLOYEES
  INNER JOIN TECH_CENTER$DB.REQUEST
  ON TECH_CENTER$DB.REQUEST.ID = TECH_CENTER$DB.REQUEST_EMPLOYEES.REQUEST_ID
  WHERE EMPLOYEE_ID = p_emp_id AND STATUS_ID = p_status_id ORDER BY REQUEST_ID DESC;

  BEGIN
    OWA_UTIL.MIME_HEADER ('application/json;charset=windows-1251');
    OPEN cur_emp_sorted_request_id;
    HTP.p('{');

    v_p_start := p_page_number * v_requests_page_amount - 7;
    v_p_end := p_page_number * v_requests_page_amount;
    SELECT COUNT(REQUEST_ID) INTO v_emp_requests_amount FROM TECH_CENTER$DB.REQUEST_EMPLOYEES
    INNER JOIN TECH_CENTER$DB.REQUEST
    ON TECH_CENTER$DB.REQUEST.ID = TECH_CENTER$DB.REQUEST_EMPLOYEES.REQUEST_ID
    WHERE EMPLOYEE_ID = p_emp_id AND STATUS_ID = p_status_id;
    v_emp_max_page := v_emp_requests_amount / 8;

    IF(v_emp_requests_amount <> 0) THEN
        HTP.p('     successful:true,');
    ELSE
        HTP.p('     successful:false,');
        HTP.p('     reason:"У Вас нет заявок",');
    END IF;

    HTP.p('     v_p_start:' || v_p_start || ',');
    HTP.p('     v_p_end:' || v_p_end || ',');
    HTP.p('     v_emp_requests_amount:' || v_emp_requests_amount || ',');
    IF(v_emp_max_page = 0) THEN
        v_emp_max_page := 1;
    END IF;

    HTP.p('     v_emp_max_page:' || v_emp_max_page || ',');

    IF(p_page_number > v_emp_max_page) THEN
        HTP.p('     error: Вы запросили страницу больше чем максимальную (' || p_page_number || ')');
    ELSE
        IF(v_emp_requests_amount <> 0 AND (v_emp_max_page = 0 OR v_emp_max_page = 1)) THEN
            HTP.p('     requests:[');
            LOOP
                FETCH cur_emp_sorted_request_id INTO p_request_id;
                EXIT WHEN cur_emp_sorted_request_id%NOTFOUND;
                -- Вносим всю заявку в переменную, чтобы распарсить её
                SELECT * INTO v_employee_request_row FROM TECH_CENTER$DB.REQUEST WHERE TECH_CENTER$DB.REQUEST.ID = p_request_id;

                -- Получить текст заяки
                SELECT INFO INTO v_request_info FROM TECH_CENTER$DB.REQUEST_WORKS WHERE REQUEST_ID = p_request_id AND ROWNUM < 2;

                -- Получить данные о статусе заявки
                SELECT STATUS_NAME, COLOR, DESCR, IMG
                INTO v_request_status_name, v_request_color, v_request_descr, v_request_img
                FROM TECH_CENTER$DB.REQUEST_STATUS WHERE ID = v_employee_request_row.status_id;

                -- Получить тип заявки
                SELECT NAME INTO v_request_type_name FROM TECH_CENTER$DB.REQUEST_TYPE WHERE ID = v_employee_request_row.type_id;

                -- Получить адрес КФУ заявки
                IF(v_employee_request_row.building_kfu_id IS NOT NULL) THEN
                    SELECT ADRES_NAME INTO v_request_building_kfu_name FROM TECH_CENTER$DB.BUILDING_KFU WHERE ID = v_employee_request_row.building_kfu_id;
                    v_emp_request_building_kfu_id := v_employee_request_row.building_kfu_id;
                ELSE
                    v_request_building_kfu_name := ' ';
                    v_emp_request_building_kfu_id := 0;
                END IF;

                  HTP.p('          {');

                  HTP.p('            id:' || v_employee_request_row.id || ',');
                  HTP.p('            emp_fio:"' || SERVICEDESK_MOBILE_TEST.GET_REQUEST_EMP_FIO(v_employee_request_row.id) || '",');
                  HTP.p('            request_date:"' || v_employee_request_row.request_date || '",');
                  HTP.p('            phone:"' || v_employee_request_row.phone || '",');
                  IF(v_employee_request_row.declarant_fio = v_employee_request_row.contact_face_fio) THEN
                    HTP.p('            declarant_fio:"' || v_employee_request_row.declarant_fio || '",');
                  ELSE
                    HTP.p('            declarant_fio:"' || v_employee_request_row.declarant_fio || '",');
                    HTP.p('            contact_face_fio:"' || v_employee_request_row.contact_face_fio || '",');
                  END IF;
                  HTP.p('            info:"' || SERVICEDESK_MOBILE.removeDoubleQuotesFromString(v_request_info) || '",');
                  HTP.p('            request_priority:' || v_employee_request_row.request_priority || ',');
                  HTP.p('            date_of_realization:"' || v_employee_request_row.date_of_realization || '",');
                  HTP.p('            status:{');
                  HTP.p('               id:' || p_request_id || ',');
                  HTP.p('               status_name:"' || v_request_status_name || '",');
                  HTP.p('               color:"' || v_request_color || '",');
                  HTP.p('               descr:"' || v_request_descr || '"');
                  HTP.p('            },');
                  --HTP.p('            info:"' || SERVICEDESK_MOBILE.removeDoubleQuotesFromString(v_employee_request_row.reason) || '",');
                  HTP.p('            cod:' || v_employee_request_row.cod || ',');
                  HTP.p('            type:{');
                  HTP.p('               id:' || v_employee_request_row.type_id  || ',');
                  HTP.p('               name:"' || v_request_type_name || '"');
                  HTP.p('            },');
                  HTP.p('            post:"' || v_employee_request_row.post || '",');
                  HTP.p('            date_of_reg:"' || v_employee_request_row.date_of_reg || '",');
                  HTP.p('            reg_user:' || v_employee_request_row.reg_user || ',');
                  IF(v_employee_request_row.zaavitel IS NOT NULL) THEN
                    HTP.p('            zaavitel:' || v_employee_request_row.zaavitel || ',');
                  ELSE
                    HTP.p('            zaavitel:' || 0 || ',');
                  END IF;
                  HTP.p('            building_kfu:{');
                  HTP.p('               id:' || v_emp_request_building_kfu_id || ',');
                  HTP.p('               name:"' || v_request_building_kfu_name || '"');
                  HTP.p('            },');
                  HTP.p('            room_num:"' || v_employee_request_row.room_num || '"');

                  IF(v_counter != v_emp_requests_amount) THEN
                    HTP.p('        },');
                    v_counter := v_counter + 1;
                  ELSE
                    HTP.p('        }');
                  END IF;
            END LOOP;
            HTP.p('     ]');
            CLOSE cur_emp_sorted_request_id;
        END IF;

        IF(v_emp_requests_amount <> 0 AND v_emp_max_page > 1) THEN
            HTP.p('     requests:[');
            LOOP
                FETCH cur_emp_sorted_request_id INTO p_request_id;
                EXIT WHEN cur_emp_sorted_request_id%NOTFOUND OR cur_emp_sorted_request_id%ROWCOUNT > v_p_end;

                v_range := v_range + 1;

               IF(v_range >= v_p_start AND v_range <= v_p_end) THEN       -- 1;8 | 9;16 | 17;24

                    -- Вносим всю заявку в переменную, чтобы распарсить её
                    SELECT * INTO v_employee_request_row FROM TECH_CENTER$DB.REQUEST WHERE TECH_CENTER$DB.REQUEST.ID = p_request_id;

                    -- Получить текст заяки
                    SELECT INFO INTO v_request_info FROM TECH_CENTER$DB.REQUEST_WORKS WHERE REQUEST_ID = p_request_id AND ROWNUM < 2;

                    -- Получить данные о статусе заявки
                    SELECT STATUS_NAME, COLOR, DESCR, IMG
                    INTO v_request_status_name, v_request_color, v_request_descr, v_request_img
                    FROM TECH_CENTER$DB.REQUEST_STATUS WHERE ID = v_employee_request_row.status_id;

                    -- Получить тип заявки
                    SELECT NAME INTO v_request_type_name FROM TECH_CENTER$DB.REQUEST_TYPE WHERE ID = v_employee_request_row.type_id;

                    -- Получить адрес КФУ заявки
                    IF(v_employee_request_row.building_kfu_id IS NOT NULL) THEN
                        SELECT ADRES_NAME INTO v_request_building_kfu_name FROM TECH_CENTER$DB.BUILDING_KFU WHERE ID = v_employee_request_row.building_kfu_id;
                        v_emp_request_building_kfu_id := v_employee_request_row.building_kfu_id;
                    ELSE
                        v_request_building_kfu_name := ' ';
                        v_emp_request_building_kfu_id := 0;
                    END IF;

                      HTP.p('          {');

                      HTP.p('            id:' || v_employee_request_row.id || ',');
                      HTP.p('            emp_fio:"' || SERVICEDESK_MOBILE_TEST.GET_REQUEST_EMP_FIO(v_employee_request_row.id) || '",');
                      HTP.p('            request_date:"' || v_employee_request_row.request_date || '",');
                      HTP.p('            phone:"' || v_employee_request_row.phone || '",');
                      IF(v_employee_request_row.declarant_fio = v_employee_request_row.contact_face_fio) THEN
                        HTP.p('            declarant_fio:"' || v_employee_request_row.declarant_fio || '",');
                      ELSE
                        HTP.p('            declarant_fio:"' || v_employee_request_row.declarant_fio || '",');
                        HTP.p('            contact_face_fio:"' || v_employee_request_row.contact_face_fio || '",');
                      END IF;
                      HTP.p('            info:"' || SERVICEDESK_MOBILE.removeDoubleQuotesFromString(v_request_info) || '",');
                      HTP.p('            request_priority:' || v_employee_request_row.request_priority || ',');
                      HTP.p('            date_of_realization:"' || v_employee_request_row.date_of_realization || '",');
                      HTP.p('            status:{');
                      HTP.p('               id:' || p_request_id || ',');
                      HTP.p('               status_name:"' || v_request_status_name || '",');
                      HTP.p('               color:"' || v_request_color || '",');
                      HTP.p('               descr:"' || v_request_descr || '"');
                      HTP.p('            },');
                      --HTP.p('            info:"' || SERVICEDESK_MOBILE.removeDoubleQuotesFromString(v_employee_request_row.reason) || '",');
                      HTP.p('            cod:' || v_employee_request_row.cod || ',');
                      HTP.p('            type:{');
                      HTP.p('               id:' || v_employee_request_row.type_id  || ',');
                      HTP.p('               name:"' || v_request_type_name || '"');
                      HTP.p('            },');
                      HTP.p('            post:"' || v_employee_request_row.post || '",');
                      HTP.p('            date_of_reg:"' || v_employee_request_row.date_of_reg || '",');
                      HTP.p('            reg_user:' || v_employee_request_row.reg_user || ',');
                      IF(v_employee_request_row.zaavitel IS NOT NULL) THEN
                        HTP.p('            zaavitel:' || v_employee_request_row.zaavitel || ',');
                      ELSE
                        HTP.p('            zaavitel:' || 0 || ',');
                      END IF;
                      HTP.p('            building_kfu:{');
                      HTP.p('               id:' || v_emp_request_building_kfu_id || ',');
                      HTP.p('               name:"' || v_request_building_kfu_name || '"');
                      HTP.p('            },');
                      HTP.p('            room_num:"' || v_employee_request_row.room_num || '"');

                      IF(v_p_start = 1) THEN
                          IF(v_counter < v_requests_page_amount - 1) THEN
                            HTP.p('        },');
                            v_counter := v_counter + 1;
                          ELSE
                            HTP.p('        }');
                          END IF;
                      ELSE
                         IF(v_counter < v_requests_page_amount) THEN
                            HTP.p('        },');
                            v_counter := v_counter + 1;
                         ELSE
                            HTP.p('        }');
                         END IF;
                      END IF;
               END IF;
            END LOOP;
            HTP.p('     ]');
            CLOSE cur_emp_sorted_request_id;
        END IF;

    END IF;

    HTP.p('}');

  END;

  PROCEDURE get_current_requests(
      p_page_number IN INTEGER /* номер страницы с заявками  */,
      p_status_id IN INTEGER /* статус заявки  */
  )

  AS

   /***********************

    Получить теккуще заявки

  *************************/

  v_current_requests_amount INTEGER;    -- всего текущих запросов
  v_requests_page_amount CONSTANT INTEGER := 8;     -- количество запросов на одной странице
  v_p_start INTEGER := 1;       -- начальная страница
  v_p_end INTEGER := 8;     -- последняя страница
  v_max_page INTEGER;   -- самая последняя возможная страница
  p_current_request_id TECH_CENTER$DB.REQUEST.ID%TYPE;
  v_counter INTEGER := 1; -- счетчик, чтобы понять ставить запятую или не ствить ее после } в списке

  v_request_row TECH_CENTER$DB.REQUEST%ROWTYPE; -- tuple заявки с tech_center$db.request
  v_request_info TECH_CENTER$DB.REQUEST_WORKS.INFO%TYPE; -- текст заявки
  v_request_status_name VARCHAR2(30); -- название статуса заявки
  v_request_color VARCHAR2(6); -- цвет статуса заявки
  v_request_descr VARCHAR2(75); -- описание статуса заявки
  v_request_img VARCHAR2(100); -- картинка статуса заявки
  v_request_type_name VARCHAR2(100); -- тип заявки
  v_request_building_kfu_name VARCHAR2(100); -- адрес филиала КФУ, НЕ ПОДРАЗДЕЛЕНИЕ
  v_request_building_kfu_id TECH_CENTER$DB.BUILDING_KFU.ID%TYPE; -- id здания кфу
  v_range INTEGER := 1;

  CURSOR cur_current_requests_id IS SELECT ID FROM TECH_CENTER$DB.REQUEST WHERE STATUS_ID = p_status_id ORDER BY ID DESC;

  BEGIN
    OWA_UTIL.MIME_HEADER ('application/json;charset=windows-1251');
    HTP.p('{');
    v_p_start := p_page_number * v_requests_page_amount - 7;
    v_p_end := p_page_number * v_requests_page_amount;
    SELECT COUNT(ID) INTO v_current_requests_amount FROM TECH_CENTER$DB.REQUEST WHERE STATUS_ID = p_status_id;
    v_max_page := v_current_requests_amount / 8;

    IF(v_current_requests_amount <> 0) THEN
        HTP.p('     successful:true,');
    ELSE
        HTP.p('     successful:false,');
        HTP.p('     reason:"У Вас нет заявок"');
    END IF;

    HTP.p('     v_p_start:' || v_p_start || ',');
    HTP.p('     v_p_end:' || v_p_end || ',');
    HTP.p('     v_current_requests_amount:' || v_current_requests_amount || ',');
    HTP.p('     v_max_page:' || v_max_page || ',');

    IF(p_page_number > v_max_page) THEN
        HTP.p('     error: Вы запросили страницу больше чем максимальную (' || p_page_number || ')');
    ELSE

        OPEN cur_current_requests_id;

        IF(v_current_requests_amount <> 0 AND (v_max_page = 0 OR v_max_page = 1)) THEN
            HTP.p('     requests:[');
            LOOP
                FETCH cur_current_requests_id INTO p_current_request_id;
                EXIT WHEN cur_current_requests_id%NOTFOUND;
                -- Вносим всю заявку в переменную, чтобы распарсить её
                SELECT * INTO v_request_row FROM TECH_CENTER$DB.REQUEST WHERE TECH_CENTER$DB.REQUEST.ID = p_current_request_id;

                -- Получить текст заяки
                SELECT INFO INTO v_request_info FROM TECH_CENTER$DB.REQUEST_WORKS WHERE REQUEST_ID = p_current_request_id AND ROWNUM < 2;

                -- Получить данные о статусе заявки
                SELECT STATUS_NAME, COLOR, DESCR, IMG
                INTO v_request_status_name, v_request_color, v_request_descr, v_request_img
                FROM TECH_CENTER$DB.REQUEST_STATUS WHERE ID = v_request_row.status_id;

                -- Получить тип заявки
                SELECT NAME INTO v_request_type_name FROM TECH_CENTER$DB.REQUEST_TYPE WHERE ID = v_request_row.type_id;

                -- Получить адрес КФУ заявки
                IF(v_request_row.building_kfu_id IS NOT NULL) THEN
                    SELECT ADRES_NAME INTO v_request_building_kfu_name FROM TECH_CENTER$DB.BUILDING_KFU WHERE ID = v_request_row.building_kfu_id;
                    v_request_building_kfu_id := v_request_row.building_kfu_id;
                ELSE
                    v_request_building_kfu_name := ' ';
                    v_request_building_kfu_id := 0;
                END IF;

                  HTP.p('          {');

                  HTP.p('            id:' || v_request_row.id || ',');
                  HTP.p('            emp_fio:"' || SERVICEDESK_MOBILE_TEST.GET_REQUEST_EMP_FIO(v_request_row.id) || '",');
                  HTP.p('            request_date:"' || v_request_row.request_date || '",');
                  HTP.p('            phone:"' || v_request_row.phone || '",');
                  IF(v_request_row.declarant_fio = v_request_row.contact_face_fio) THEN
                    HTP.p('            declarant_fio:"' || v_request_row.declarant_fio || '",');
                  ELSE
                    HTP.p('            declarant_fio:"' || v_request_row.declarant_fio || '",');
                    HTP.p('            contact_face_fio:"' || v_request_row.contact_face_fio || '",');
                  END IF;
                  HTP.p('            info:"' || SERVICEDESK_MOBILE.removeDoubleQuotesFromString(v_request_info) || '",');
                  HTP.p('            request_priority:' || v_request_row.request_priority || ',');
                  HTP.p('            date_of_realization:"' || v_request_row.date_of_realization || '",');
                  HTP.p('            status:{');
                  HTP.p('               id:' || p_current_request_id || ',');
                  HTP.p('               status_name:"' || v_request_status_name || '",');
                  HTP.p('               color:"' || v_request_color || '",');
                  HTP.p('               descr:"' || v_request_descr || '"');
                  HTP.p('            },');
                  HTP.p('            reason:"' || SERVICEDESK_MOBILE.removeDoubleQuotesFromString(v_request_row.reason) || '",');
                  HTP.p('            cod:' || v_request_row.cod || ',');
                  HTP.p('            type:{');
                  HTP.p('               id:' || v_request_row.type_id  || ',');
                  HTP.p('               name:"' || v_request_type_name || '"');
                  HTP.p('            },');
                  HTP.p('            post:"' || v_request_row.post || '",');
                  HTP.p('            date_of_reg:"' || v_request_row.date_of_reg || '",');
                  IF(v_request_row.reg_user IS NULL) THEN
                    HTP.p('            reg_user:' || 0 || ',');
                  ELSE
                    HTP.p('            reg_user:' || v_request_row.reg_user || ',');
                  END IF;
                  IF(v_request_row.zaavitel IS NOT NULL) THEN
                    HTP.p('            zaavitel:' || v_request_row.zaavitel || ',');
                  ELSE
                    HTP.p('            zaavitel:' || 0 || ',');
                  END IF;
                  HTP.p('            building_kfu:{');
                  HTP.p('               id:' || v_request_building_kfu_id || ',');
                  HTP.p('               name:"' || v_request_building_kfu_name || '"');
                  HTP.p('            },');
                  HTP.p('            room_num:"' || v_request_row.room_num || '"');

                  IF(v_counter != v_current_requests_amount) THEN
                    HTP.p('        },');
                    v_counter := v_counter + 1;
                  ELSE
                    HTP.p('        }');
                  END IF;
            END LOOP;
            HTP.p('     ]');
            CLOSE cur_current_requests_id;
        END IF;

        IF(v_current_requests_amount <> 0 AND v_max_page > 1) THEN
            HTP.p('     requests:[');
            LOOP
                FETCH cur_current_requests_id INTO p_current_request_id;
                EXIT WHEN cur_current_requests_id%NOTFOUND OR cur_current_requests_id%ROWCOUNT > v_p_end;

                v_range := v_range + 1;

                IF(v_range >= v_p_start AND v_range <= v_p_end) THEN       -- 1;8 | 9;16 | 17;24

                    -- Вносим всю заявку в переменную, чтобы распарсить её
                    SELECT * INTO v_request_row FROM TECH_CENTER$DB.REQUEST WHERE TECH_CENTER$DB.REQUEST.ID = p_current_request_id;

                    -- Получить текст заяки
                    SELECT INFO INTO v_request_info FROM TECH_CENTER$DB.REQUEST_WORKS WHERE REQUEST_ID = p_current_request_id AND ROWNUM < 2;

                    -- Получить данные о статусе заявки
                    SELECT STATUS_NAME, COLOR, DESCR, IMG
                    INTO v_request_status_name, v_request_color, v_request_descr, v_request_img
                    FROM TECH_CENTER$DB.REQUEST_STATUS WHERE ID = v_request_row.status_id;

                    -- Получить тип заявки
                    SELECT NAME INTO v_request_type_name FROM TECH_CENTER$DB.REQUEST_TYPE WHERE ID = v_request_row.type_id;

                    -- Получить адрес КФУ заявки
                    IF(v_request_row.building_kfu_id IS NOT NULL) THEN
                        SELECT ADRES_NAME INTO v_request_building_kfu_name FROM TECH_CENTER$DB.BUILDING_KFU WHERE ID = v_request_row.building_kfu_id;
                        v_request_building_kfu_id := v_request_row.building_kfu_id;
                    ELSE
                        v_request_building_kfu_name := ' ';
                        v_request_building_kfu_id := 0;
                    END IF;

                      HTP.p('         {');

                      HTP.p('            id:' || v_request_row.id || ',');
                      HTP.p('            emp_fio:"' || SERVICEDESK_MOBILE_TEST.GET_REQUEST_EMP_FIO(v_request_row.id) || '",');
                      HTP.p('            request_date:"' || v_request_row.request_date || '",');
                      HTP.p('            phone:"' || v_request_row.phone || '",');
                      IF(v_request_row.declarant_fio = v_request_row.contact_face_fio) THEN
                        HTP.p('            declarant_fio:"' || v_request_row.declarant_fio || '",');
                      ELSE
                        HTP.p('            declarant_fio:"' || v_request_row.declarant_fio || '",');
                        HTP.p('            contact_face_fio:"' || v_request_row.contact_face_fio || '",');
                      END IF;
                      HTP.p('            info:"' || SERVICEDESK_MOBILE.removeDoubleQuotesFromString(v_request_info) || '",');
                      HTP.p('            request_priority:' || v_request_row.request_priority || ',');
                      HTP.p('            date_of_realization:"' || v_request_row.date_of_realization || '",');
                      HTP.p('            status:{');
                      HTP.p('               id:' || p_current_request_id || ',');
                      HTP.p('               status_name:"' || v_request_status_name || '",');
                      HTP.p('               color:"' || v_request_color || '",');
                      HTP.p('               descr:"' || v_request_descr || '"');
                      HTP.p('            },');
                      HTP.p('            reason:"' || SERVICEDESK_MOBILE.removeDoubleQuotesFromString(v_request_row.reason) || '",');
                      HTP.p('            cod:' || v_request_row.cod || ',');
                      HTP.p('            type:{');
                      HTP.p('               id:' || v_request_row.type_id  || ',');
                      HTP.p('               name:"' || v_request_type_name || '"');
                      HTP.p('            },');
                      HTP.p('            post:"' || v_request_row.post || '",');
                      HTP.p('            date_of_reg:"' || v_request_row.date_of_reg || '",');
                      IF(v_request_row.reg_user IS NULL) THEN
                        HTP.p('            reg_user:' || 0 || ',');
                      ELSE
                        HTP.p('            reg_user:' || v_request_row.reg_user || ',');
                      END IF;
                      IF(v_request_row.zaavitel IS NOT NULL) THEN
                        HTP.p('            zaavitel:' || v_request_row.zaavitel || ',');
                      ELSE
                        HTP.p('            zaavitel:' || 0 || ',');
                      END IF;
                      HTP.p('            building_kfu:{');
                      HTP.p('               id:' || v_request_building_kfu_id || ',');
                      HTP.p('               name:"' || v_request_building_kfu_name || '"');
                      HTP.p('            },');
                      HTP.p('            room_num:"' || v_request_row.room_num || '"');

                      IF(v_p_start = 1) THEN
                          IF(v_counter < v_requests_page_amount - 1) THEN
                            HTP.p('        },');
                            v_counter := v_counter + 1;
                          ELSE
                            HTP.p('        }');
                          END IF;
                      ELSE
                         IF(v_counter < v_requests_page_amount) THEN
                            HTP.p('        },');
                            v_counter := v_counter + 1;
                         ELSE
                            HTP.p('        }');
                         END IF;
                      END IF;

                    END IF;
            END LOOP;
            HTP.p('     ]');
            CLOSE cur_current_requests_id;
        END IF;

    END IF;


    HTP.p('}');

  END;


  FUNCTION get_request_emp_fio(request_id IN INTEGER /* id заявки */)
  RETURN VARCHAR2
  AS

   /***************************

    Получить ФИО исполнителя на заявку

  ****************************/

  v_emp_fio VARCHAR2(150) := ' ';
  p_request_id INTEGER := request_id;

  BEGIN

    FOR emp_name IN (SELECT STAFF$DB.EMPLOYEE.LASTNAME, STAFF$DB.EMPLOYEE.FIRSTNAME, STAFF$DB.EMPLOYEE.MIDDLENAME,
    IAS$DB.USERS.ID
    FROM TECH_CENTER$DB.REQUEST_EMPLOYEES
    INNER JOIN IAS$DB.USERS
    ON IAS$DB.USERS.ID = TECH_CENTER$DB.REQUEST_EMPLOYEES.EMPLOYEE_ID
    INNER JOIN STAFF$DB.EMPLOYEE
    ON STAFF$DB.EMPLOYEE.ID = IAS$DB.USERS.EMPLOYEE_ID
    WHERE TECH_CENTER$DB.REQUEST_EMPLOYEES.REQUEST_ID = p_request_id) LOOP

        v_emp_fio := emp_name.lastname || ' ' || emp_name.firstname || ' ' || emp_name.middlename;

    END LOOP;

    IF (v_emp_fio = ' ') THEN
        v_emp_fio := 'отсутствует';
    END IF;

    RETURN v_emp_fio;

  END;

  PROCEDURE search_request(p_sql_statement VARCHAR2 /* сформированный SQL запрос */,
    p_sql_statement_count_rows VARCHAR2 /* SQL запрос на сервер для подсчета найденных строк */,
    p_page_number INTEGER /* номер страницы */)
  AS

  /**********************************

    Функция произваодит поиск заявок

  ***********************************/

  TYPE search_req_cur_type IS REF CURSOR;
  search_req_cur   search_req_cur_type;
  request_rec  TECH_CENTER$DB.REQUEST%ROWTYPE;

  TYPE search_req_cur_type2 IS REF CURSOR;
  search_req_cur2   search_req_cur_type2;
  count_rec INTEGER;

  v_request_info TECH_CENTER$DB.REQUEST_WORKS.INFO%TYPE; -- текст заявки
  v_request_status_name VARCHAR2(30); -- название статуса заявки
  v_request_color VARCHAR2(6); -- цвет статуса заявки
  v_request_descr VARCHAR2(75); -- описание статуса заявки
  v_request_img VARCHAR2(100); -- картинка статуса заявки
  v_request_type_name VARCHAR2(100); -- тип заявки
  v_request_building_kfu_name VARCHAR2(100); -- адрес филиала КФУ, НЕ ПОДРАЗДЕЛЕНИЕ
  v_request_building_kfu_id TECH_CENTER$DB.BUILDING_KFU.ID%TYPE; -- id здания кфу

  v_p_start INTEGER := 1;       -- начальная страница
  v_p_end INTEGER := 8;     -- последняя страница
  v_max_page INTEGER;   -- самая последняя возможная страница
  v_requests_page_amount CONSTANT INTEGER := 8;     -- количество запросов на одной странице
  v_range INTEGER := 1;
  v_counter INTEGER := 1; -- счетчик, чтобы понять ставить запятую или не ствить ее после } в списке


  BEGIN
    OWA_UTIL.MIME_HEADER ('application/json;charset=windows-1251');
    HTP.p('{');

    v_p_start := p_page_number * v_requests_page_amount - 7;
    v_p_end := p_page_number * v_requests_page_amount;

    HTP.p('     p_sql_statement:"' || p_sql_statement || '",');
    HTP.p('     p_sql_statement_count_rows:"' || p_sql_statement_count_rows || '",');

    OPEN search_req_cur2 FOR p_sql_statement_count_rows;
    LOOP
        FETCH search_req_cur2 INTO count_rec;
        EXIT WHEN search_req_cur2%NOTFOUND;
        HTP.p('     requests_amount:' || count_rec || ',');
    END LOOP;

    v_max_page := count_rec / 8;

    HTP.p('     v_p_start:' || v_p_start || ',');
    HTP.p('     v_p_end:' || v_p_end || ',');
    HTP.p('     v_max_page:' || v_max_page || ',');

    CLOSE search_req_cur2;

    IF(p_page_number > v_max_page) THEN
        HTP.p('     error: Вы запросили страницу больше чем максимальную (' || p_page_number || ')');
    ELSE

        HTP.p('     requests:[');

        OPEN search_req_cur FOR p_sql_statement;

        IF(count_rec <> 0 AND (v_max_page = 0 OR v_max_page = 1)) THEN
            LOOP
                FETCH search_req_cur INTO request_rec;
                EXIT WHEN search_req_cur%NOTFOUND;
                -- Получить текст заяки
                SELECT INFO INTO v_request_info FROM TECH_CENTER$DB.REQUEST_WORKS WHERE REQUEST_ID = request_rec.id AND ROWNUM < 2;

                -- Получить данные о статусе заявки
                SELECT STATUS_NAME, COLOR, DESCR, IMG
                INTO v_request_status_name, v_request_color, v_request_descr, v_request_img
                FROM TECH_CENTER$DB.REQUEST_STATUS WHERE ID = request_rec.status_id;

                -- Получить тип заявки
                SELECT NAME INTO v_request_type_name FROM TECH_CENTER$DB.REQUEST_TYPE WHERE ID = request_rec.type_id;

                -- Получить адрес КФУ заявки
                IF(request_rec.building_kfu_id IS NOT NULL) THEN
                    SELECT ADRES_NAME INTO v_request_building_kfu_name FROM TECH_CENTER$DB.BUILDING_KFU WHERE ID = request_rec.building_kfu_id;
                    v_request_building_kfu_id := request_rec.building_kfu_id;
                ELSE
                    v_request_building_kfu_name := ' ';
                    v_request_building_kfu_id := 0;
                END IF;

                  HTP.p('         {');

                  HTP.p('            id:' || request_rec.id || ',');
                  HTP.p('            emp_fio:"' || SERVICEDESK_MOBILE_TEST.GET_REQUEST_EMP_FIO(request_rec.id) || '",');
                  HTP.p('            request_date:"' || request_rec.request_date || '",');
                  HTP.p('            phone:"' || request_rec.phone || '",');
                  IF(request_rec.declarant_fio = request_rec.contact_face_fio) THEN
                    HTP.p('            declarant_fio:"' || request_rec.declarant_fio || '",');
                  ELSE
                    HTP.p('            declarant_fio:"' || request_rec.declarant_fio || '",');
                    HTP.p('            contact_face_fio:"' || request_rec.contact_face_fio || '",');
                  END IF;
                  HTP.p('            info:"' || SERVICEDESK_MOBILE.removeDoubleQuotesFromString(v_request_info) || '",');
                  HTP.p('            request_priority:' || request_rec.request_priority || ',');
                  HTP.p('            date_of_realization:"' || request_rec.date_of_realization || '",');
                  HTP.p('            status:{');
                  HTP.p('               id:' || request_rec.id || ',');
                  HTP.p('               status_name:"' || v_request_status_name || '",');
                  HTP.p('               color:"' || v_request_color || '",');
                  HTP.p('               descr:"' || v_request_descr || '"');
                  HTP.p('            },');
                  HTP.p('            reason:"' || SERVICEDESK_MOBILE.removeDoubleQuotesFromString(request_rec.reason) || '",');
                  HTP.p('            cod:' || request_rec.cod || ',');
                  HTP.p('            type:{');
                  HTP.p('               id:' || request_rec.type_id  || ',');
                  HTP.p('               name:"' || v_request_type_name || '"');
                  HTP.p('            },');
                  HTP.p('            post:"' || request_rec.post || '",');
                  HTP.p('            date_of_reg:"' || request_rec.date_of_reg || '",');
                  IF(request_rec.reg_user IS NULL) THEN
                    HTP.p('            reg_user:' || 0 || ',');
                  ELSE
                    HTP.p('            reg_user:' || request_rec.reg_user || ',');
                  END IF;
                  IF(request_rec.zaavitel IS NOT NULL) THEN
                    HTP.p('            zaavitel:' || request_rec.zaavitel || ',');
                  ELSE
                    HTP.p('            zaavitel:' || 0 || ',');
                  END IF;
                  HTP.p('            building_kfu:{');
                  HTP.p('               id:' || v_request_building_kfu_id || ',');
                  HTP.p('               name:"' || v_request_building_kfu_name || '"');
                  HTP.p('            },');
                  HTP.p('            room_num:"' || request_rec.room_num || '"');

                  IF(search_req_cur%ROWCOUNT < count_rec) THEN
                    HTP.p('         },');
                  ELSE
                    HTP.p('         }');
                  END IF;

            END LOOP;
        END IF;

        IF(count_rec <> 0 AND v_max_page > 1) THEN
            LOOP
                FETCH search_req_cur INTO request_rec;
                EXIT WHEN search_req_cur%NOTFOUND OR search_req_cur%ROWCOUNT > v_p_end;

                v_range := v_range + 1;

                IF(v_range >= v_p_start AND v_range <= v_p_end) THEN       -- 1;8 | 9;16 | 17;24

                    -- Получить текст заяки
                    SELECT INFO INTO v_request_info FROM TECH_CENTER$DB.REQUEST_WORKS WHERE REQUEST_ID = request_rec.id AND ROWNUM < 2;

                    -- Получить данные о статусе заявки
                    SELECT STATUS_NAME, COLOR, DESCR, IMG
                    INTO v_request_status_name, v_request_color, v_request_descr, v_request_img
                    FROM TECH_CENTER$DB.REQUEST_STATUS WHERE ID = request_rec.status_id;

                    -- Получить тип заявки
                    SELECT NAME INTO v_request_type_name FROM TECH_CENTER$DB.REQUEST_TYPE WHERE ID = request_rec.type_id;

                    -- Получить адрес КФУ заявки
                    IF(request_rec.building_kfu_id IS NOT NULL) THEN
                        SELECT ADRES_NAME INTO v_request_building_kfu_name FROM TECH_CENTER$DB.BUILDING_KFU WHERE ID = request_rec.building_kfu_id;
                        v_request_building_kfu_id := request_rec.building_kfu_id;
                    ELSE
                        v_request_building_kfu_name := ' ';
                        v_request_building_kfu_id := 0;
                    END IF;

                      HTP.p('         {');

                      HTP.p('            id:' || request_rec.id || ',');
                      HTP.p('            emp_fio:"' || SERVICEDESK_MOBILE_TEST.GET_REQUEST_EMP_FIO(request_rec.id) || '",');
                      HTP.p('            request_date:"' || request_rec.request_date || '",');
                      HTP.p('            phone:"' || request_rec.phone || '",');
                      IF(request_rec.declarant_fio = request_rec.contact_face_fio) THEN
                        HTP.p('            declarant_fio:"' || request_rec.declarant_fio || '",');
                      ELSE
                        HTP.p('            declarant_fio:"' || request_rec.declarant_fio || '",');
                        HTP.p('            contact_face_fio:"' || request_rec.contact_face_fio || '",');
                      END IF;
                      HTP.p('            info:"' || SERVICEDESK_MOBILE.removeDoubleQuotesFromString(v_request_info) || '",');
                      HTP.p('            request_priority:' || request_rec.request_priority || ',');
                      HTP.p('            date_of_realization:"' || request_rec.date_of_realization || '",');
                      HTP.p('            status:{');
                      HTP.p('               id:' || request_rec.id || ',');
                      HTP.p('               status_name:"' || v_request_status_name || '",');
                      HTP.p('               color:"' || v_request_color || '",');
                      HTP.p('               descr:"' || v_request_descr || '"');
                      HTP.p('            },');
                      HTP.p('            reason:"' || SERVICEDESK_MOBILE.removeDoubleQuotesFromString(request_rec.reason) || '",');
                      HTP.p('            cod:' || request_rec.cod || ',');
                      HTP.p('            type:{');
                      HTP.p('               id:' || request_rec.type_id  || ',');
                      HTP.p('               name:"' || v_request_type_name || '"');
                      HTP.p('            },');
                      HTP.p('            post:"' || request_rec.post || '",');
                      HTP.p('            date_of_reg:"' || request_rec.date_of_reg || '",');
                      IF(request_rec.reg_user IS NULL) THEN
                        HTP.p('            reg_user:' || 0 || ',');
                      ELSE
                        HTP.p('            reg_user:' || request_rec.reg_user || ',');
                      END IF;
                      IF(request_rec.zaavitel IS NOT NULL) THEN
                        HTP.p('            zaavitel:' || request_rec.zaavitel || ',');
                      ELSE
                        HTP.p('            zaavitel:' || 0 || ',');
                      END IF;
                      HTP.p('            building_kfu:{');
                      HTP.p('               id:' || v_request_building_kfu_id || ',');
                      HTP.p('               name:"' || v_request_building_kfu_name || '"');
                      HTP.p('            },');
                      HTP.p('            room_num:"' || request_rec.room_num || '"');

                      IF(v_p_start = 1) THEN
                          IF(v_counter < v_requests_page_amount - 1) THEN
                            HTP.p('        },');
                            v_counter := v_counter + 1;
                          ELSE
                            HTP.p('        }');
                          END IF;
                      ELSE
                         IF(v_counter < count_rec - (v_max_page-1) * 7) THEN
                            HTP.p('        },');
                            v_counter := v_counter + 1;
                         ELSE
                            HTP.p('        }');
                         END IF;
                      END IF;

                END IF;
            END LOOP;
        END IF;

        HTP.p('     ]');
        CLOSE search_req_cur;
    END IF;
    HTP.p('}');
  END;

END SERVICEDESK_MOBILE_TEST;
/