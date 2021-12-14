package muon.app.common.i18n;

import java.util.ListResourceBundle;

public class Messages_ru extends ListResourceBundle {

    private final Object[][] resources = {

            {"general", "Главная"},
            {"terminal", "Терминал"},
            {"editor", "Редактор"},
            {"display", "Экран"},
            {"security", "Безопасность"},
            {"sessions", "Сессии"},
            {"settings", "Настройки"},
            {"add", "Добавить"},
            {"chk_update", "Проверить обновления"},
            {"new_site", "Новый сайт"},
            {"new_folder", "Новая папка"},
            {"duplicate", "Дублировать"},
            {"connect", "Подключиться"},
            {"cancel", "Отмена"},
            {"export", "Экспорт"},
            {"import", "Импорт"},
            {"name", "Имя"},
            {"connecting", "Подключение..."},
            {"import_from", "Импортировать из"},
            {"import_sessions", "Импортировать сессии"},
            {"save", "Сохранить"},
            {"reset", "Сбросить"},
            {"remove", "Удалить"},
            {"edit", "Редактировать"},
            {"generate", "Сгенерировать"},
            {"new_entry", "New entry"},
            {"connection", "Соединение"},
            {"directories", "Директории"},
            {"proxy", "Прокси"},
            {"jump_hosts", "Jump Hosts"},
            {"port_forwarding", "Переадресация портов"},
            {"user", "Пользователь"},
            {"host", "Хост"},
            {"port", "Порт"},
            {"password", "Пароль"},
            {"local_folder", "Локальная папка"},
            {"remote_folder", "Удаленная папка"},
            {"browse", "Просмотреть"},
            {"session_manager", "Менеджер сессий"},
            {"private_key_file", "Файл закрытого ключа"},
            {"proxy_type", "Тип прокси"},
            {"proxy_host", "Хост прокси"},
            {"proxy_port", "Порт прокси"},
            {"proxy_user", "Пользователь прокси"},
            {"proxy_password", "Пароль прокси"},
            {"warning_plain_text", " ( Внимание: пароль будет сохранен в виде обычного текста! )"},
            {"overwrite", "Перезаписать"},
            {"auto_rename", "Автопереименование"},
            {"skip", "Пропустить"},
            {"prompt", "Prompt"},
            {"settings_saved", "Настройки были сброшены,\n пожалуйста, сохраните и перезапустите приложение"},
            {"columns", "Колонки"},
            {"rows", "Строки"},
            {"font_name", "Шрифт"},
            {"font_size", "Размер шрифта"},
            {"copy_like_putty", "Вставка-копирование как в PuTTY (копировать при выделении и вставить на правую кнопку мыши)"},
            {"terminal_type", "Тип терминала"},
            {"initial_terminal_type", "Тип исходного терминала"},
            {"sound", "Звук"},
            {"terminal_font", "Шрифт терминала"},
            {"misc", "Разное"},
            {"terminal_colors", "Цвета и темы терминала"},
            {"terminal_theme", "Тема терминала"},
            {"default_color", "Цвет по умолчанию"},
            {"text", "Текст"},
            {"background", "Фон"},
            {"selection_color", "Цвет выделения"},
            {"search_pattern", "Search pattern"},
            {"color_palette", "Цветовая гамма"},
            {"terminal_shortcuts", "Горячие клавиши терминала"},
            {"confirm_delete_files", "Подтверждение перед удалением файлов"},
            {"confirm_move_files", "Подтверждение перед перемещением или копированием файлов"},
            {"show_hidden_files", "Показывать скрытые файлы по умолчанию"},
            {"prompt_for_sudo", "Спросить sudo, если операция не удалась из-за проблем с правами"},
            {"directory_caching", "Использовать кэширование каталогов"},
            {"current_folder", "Показывать текущую папку в панели путей"},
            {"show_banner", "Показать баннер"},
            {"word_wrap", "Переносить слова в инструменте просмотра логов"},
            {"transfer_normally", "Передача в нормальном режиме"},
            {"transfer_background", "Передача в фоновом режиме"},
            {"transfer_mode", "Режим передачи"},
            {"conflict_action", "Действие при конфликте"},
            {"add_editor", "+ Добавить редактор"},
            {"remove_editor", "- Удалить редактор"},
            {"editor_name", "Название редактора"},
            {"add_editor2", "Добавить редактор?"},
            {"zoom_text", "Зум (Уменьшите или увеличьте размер приложения на экране)"},
            {"global_dark_theme", "Использовать тёмную тему глобально (требуется перезапуск)"},
            {"zoom_percentage", "Процент зума"},
            {"new_master_password", "Новый мастер-пароль"},
            {"reenter_master_password", "Повторно введите мастер-пароль"},
            {"master_password", "Мастер-пароль"},
            {"use_master_password", "Использовать мастер-пароль"},
            {"change_master_password", "Изменить мастер-пароль"},
            {"change_password_failed", "Смена пароля не удалась!"},
            {"error_operation", "Во время работы возникла ошибка"},
            {"password_aes", "Ваши сохраненные пароли защищены шифрованием AES"},
            {"password_unprotected", "Ваши сохраненные пароли теперь не защищены"},
            {"password_no_match", "Пароли не совпадают"},
            {"unsupported_key", "Данный формат ключа не поддерживается, пожалуйста, преобразуйте его в формат OpenSSH"},
            {"copy", "Копировать"},
            {"paste", "Вставить"},
            {"select_all", "Выбрать всё"},
            {"clear_buffer", "Очистить буфер"},
            {"find", "Найти"},
            {"cut", "Вырезать"},
            {"path_executable", "Путь к исполняемому файлу"},
            {"file_browser", "Браузер файлов"},
            {"server_logs", "Логи сервера"},
            {"file_search", "Поиск файла"},
            {"diskspace", "Место на диске"},
            {"toolbox", "Инструментарий"},
            {"processes", "Процессы"},
            {"pid", "PID"},
            {"total_processes", "Всего процессов:"},
            {"last_updated", "Последнее обновление:"},
            {"analize_folder", "Анализировать папку"},
            {"analize_volume", "Анализировать том"},
            {"next", "Вперед"},
            {"back", "Назад"},
            {"reload", "Перезагрузить"},
            {"modified", "Изменено"},
            {"size", "Размер"},
            {"type", "Тип"},
            {"permission", "Права"},
            {"owner", "Владелец"},
            {"show_hidden_files2", "Показать скрытые файлы"},
            {"bookmarks", "Заметки"},
            {"select_partition", "Пожалуйста, выберите раздел"},
            {"select_volume", "Пожалуйста, выберите том"},
            {"filesystem", "Файловая система"},
            {"total_size", "Полный размер"},
            {"used", "Использовано"},
            {"available", "Доступно"},
            {"percentage_use", "% использовано"},
            {"mount_point", "Смонтировано"},
            {"directory_usage", "Дисковое использование директории"},
            {"start_another_analysis", "Начать другой анализ"},
            {"delete", "Удалить"},
            {"send_files", "Отправить файлы"},
            {"add_from_manager", "Добавление из менеджера сессий"},
            {"add_log", "Добавить лог"},
            {"insert", "Вставить"},
            {"open", "Открыть"},
            {"rename", "Переименовать"},
            {"new_file", "Новый файл"},
            {"bookmark", "Заметка"},
            {"open_new_tab", "Открыть в новой вкладке"},
            {"enter_new_name", "Пожалуйста, введите новое имя"},
            {"open_in_terminal", "Открыть в терминале"},
            {"copy_path", "Скопировать путь"},
            {"searching", "Поиск"},
            {"idle", "Простой"},
            {"in_filename", "В названии файла (например, *.zip или R*ME.txt)"},
            {"in_filecontent", "В содержимом файла"},
            {"in_compressed_files", "В сжатых файлах"},
            {"search_for", "Искать"},
            {"search_in", "Искать в"},
            {"any_time", "Любое время"},
            {"this_week", "Эта неделя"},
            {"between", "В интервале"},
            {"from", "От"},
            {"to", "До"},
            {"ready", "Готово"},
            {"look_for", "Тип поиска"},
            {"both_file_folder", "И папки, и файлы"},
            {"file_only", "Только файлы"},
            {"folder_only", "Только папки"},
            {"show_location", "Показать расположение"},
            {"filename", "Имя файла"},
            {"path", "Путь"},
            {"filter", "Фильтр"},
            {"clear", "Очистить"},
            {"refresh", "Обновить"},
            {"kill", "Убить"},
            {"kill_sudo", "Убить с помощью sudo"},
            {"change_priority", "Изменить приоритет"},
            {"change_priority_sudo", "Изменить приоритет с sudo"},
            {"copy_command", "Скопировать команду"},
            {"kill_process", "Убить процесс"},
            {"system_info", "О системе"},
            {"system_load", "Нагрузка"},
            {"services_systemd", "Сервисы - systemd"},
            {"process_ports", "Процесс и порты"},
            {"ssh_keys", "SSH ключи"},
            {"network_tools", "Сетевые инструменты"},
            {"cpu_usage", "Использование CPU"},
            {"memory_usage", "Использование RAM"},
            {"swap_usage", "Использование Swap"},
            {"used2", "Используется"},
            {"generate_new_key", "Сгенерировать новый ключ"},
            {"public_key_file", "Файл публичного ключа:"},
            {"refresh_interval", "Интервал обновления"},
            {"start", "Старт"},
            {"stop", "Стоп"},
            {"restart", "Перезапустить"},
            {"reload", "Перезагрузить"},
            {"enable", "Включить"},
            {"disable", "Выключить"},
            {"actions_sudo", "Выполнять действия от суперпользователя (sudo)"},
            {"operation_failed", "Сбой операции"},
            {"status", "Статус"},
            {"state", "Состояние"},
            {"description", "Описание"},
            {"source_port", "Порт источника"},
            {"target_port", "Порт назначения"},
            {"bind_host", "Привязанный хост"},
            {"local", "Local"},
            {"remote", "Remote"},
            {"invalid_input", "Неверный ввод: все поля обязательны для заполнения"},
            {"host_ping", "Хост"},
            {"host_name", "Хост"},
            {"port_number", "Порт"},
            {"tool_use", "Команда"},
            {"executed_errors", "Выполнено с ошибками"},
            {"configure_editor", "Настроить редактор"},
            {"open_log_viewer", "Открыть с помощью инструмента просмотра логов"},
            {"open_in_tab", "Открыть в вкладке"},
            {"open_with", "Открыть с помощью..."},
            {"send_another_server", "Отправить на другой сервер"},
            {"send_over_ftp", "Отправить по ftp"},
            {"send_this_computer", "Отправить через этот компьютер"},
            {"run_in_terminal", "Выполнить в терминале"},
            {"open_folder_terminal", "Открыть папку в терминале"},
            {"open_terminal_here", "Открыть терминал здесь"},
            {"run_file_in_terminal", "Выполнить файл в терминале"},
            {"run_file_in_background", "Выполнить файл в фоновом режиме"},
            {"edit_with", "Редактировать с помощью"},
            {"properties", "Свойства"},
            {"create_link", "Создать ссылку"},
            {"extract_here", "Извлечь здесь"},
            {"extract_to", "Извлечь в"},
            {"selec_target", "Выберите конечную папку для извлечения"},
            {"create_archive", "Создать архив"},
            {"download_files", "Скачать выбранные файлы"},
            {"upload_here", "Загрузить здесь"},
            {"please_new_name", "Пожалуйста, введите новое имя"},
            {"open_as_url", "Открыть как URL"},
            {"page_up", "Страница вверх"},
            {"page_down", "Страница вниз"},
            {"line_up", "Строчка вверх"},
            {"line_down", "Строчка вниз"},
            {"search", "Поиск"},
            {"no_entry_selected", "Ни одна запись не была выбрана"},
            {"authorized_keys", "Авторизованные ключи"},
            {"local_computer", "Локальный компьютер"},
            {"server", "Сервер"},
            {"language", "Язык"},
            {"log_viewer_lines", "Строк на странице в инструменте просмотра логов"},
            {"log_viewer_font_size", "Размер шрифта инструмента просмотра логов"},
            {"system_refresh_interval", "Интервал обновления загрузки системы (сек) "},
            {"autorename", "Автопереименование"},
            {"use_sudo_if_fails", "Использовать sudo, если операция не удалась из-за проблем с правами (осторожно!)"},
            {"transfer_temporary_directory", "Перенос файлов во временный каталог при неудачной операции из-за проблем с правами"},
            {"show_filebrowser_first", "Показывать бразуер файлов первым"},
            {"connection_timeout", "Таймаут соединения"},
            {"show", "Показать"},
            {"keep_alive", "Keep connection alive"}
    };

    @Override
    protected Object[][] getContents() {

        return resources;
    }
}
