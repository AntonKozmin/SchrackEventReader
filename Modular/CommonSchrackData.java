package EvntFileReader;

class CommonSchrackData {

    static final public String[] ADDINFO = {null, // 0
        "конец текущего состояния", // 1
        "повторный опрос", // 2
        "завершен повторный опрос", // 3
        null, // 4
        "конец состояния- у устройств в группе различ. состояния"// 5
    };

    static final public String[] STATES = {"покой", // 0
        "тревога", // 1
        "ревизия-тревога", // 2
        "неисправность", // 3
        "ревизия-неисправность", // 4
        "отключение", // 5
        "внут. отключение", // 6
        "ревизия", // 7
        "активация", // 8
        "деактивация", // 9
        "тревога с передачей на ПЦН", // 10
        "загрязнение датчика", // 11
        "включено", // 12
        "вход-активация", // 13
        "вход-активация в режиме ревизии", // 14
        "предварительный сигнал тревоги", // 15
        "останов тревоги оператором", // 16
        "прервано", // 17
        "ревизия-активация", // 18
        "подача бумаги", // 19
        "скрытая тревога", // 20
        "предактивация", // 21
        "срабатывание", // 22
        "предактивация входа", // 23
        "ревизия-предактивация входа", // 24
        "пониж. напряжение", // 25
        "предварит. сигнал", // 26
        "ревизия-предварит. сигнал", // 27
        "предтревога" // 28
};

    static final public String[] COMMANDS = {"выключение", // "0"
        "включение", // "1"
        "активация", // "2"
        "сброс", // "3"
        "сброс тревоги", // "4"
        "сброс акустики", // "5"
        "сброс параметр. запыления", // "6"
        "ревизия вкл.", // "7"
        "перепроверка тревоги", // "8"
        "имитация тревоги", // "9"
        "имитация неисправ.", // "10"
        "отключение", // "11"
        "имитация сработки", // "12"
        "повторная активация", // "13"
        "повторная активация акустики", // "14"
        "вкл. подачу бумаги", // "15"
        "выкл. подачу бумаги", // "16"
        "инициализация", // "17"
        "к началу страницы", // "18"
        "сброс номера страницы", // "19"
        "сброс номера сообщения", // "20"
        "0-й уровень", // "21"
        "1-й уровень", // "22"
        "2-й уровень", // "23"
        "3-й уровень", // "24"
        "выкл. звук. сигнала", // "25"
        "вкл. звук. сигнала", // "26"
        "адресация", // "27"
        "перезапуск", // "28"
        "выкл. противопож. оборудования", // "29"
        "вкл. противопож. оборудования", // "30"
        "имитация предактивации", // "31"
        "автомат. проверка шлейфа", // "32"
        "пошаговая проверка шлейфа", // "33"
        "вкл. экстренный вызов", // "34"
        "выкл. экстренный вызов", // "35"
        "активация критич. выхода", // "36"
        "скрытая ревизия", // "37"
        "тест. ревизия", // "38"
        "огранич. отключ.", // "39"
        "OnAt",// "40"
};

    private final static String[][] SUBSTATES = {
        {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, "имитация тревоги", "тревога-дымовой датчик", "тревога-тепловой датчик", "вскрытие",
            "перепроверка тревоги", "тревога не подтверждена", "тревога-СО датчик",
            "совмещенный тепловой-дымовой", "совмещенный СО-дымовой", "совмещенный СО-тепловой",
            "совмещенный СО тепловой дымовой", "датчик давления", "совмещенный тепловой давления"},
        {null, null, "тревога-дымовой датчик", "тревога-тепл. датчик", null, null, "вскрытие", "СО датчик",
            "совмещенный тепловой дымовой", "совмещенный СО дымовой", "совмещенный СО тепловой",
            "совмещенный СО тепловой дымовой", "датчик давления", "совмещенный тепловой давления"},
        {null, "неиспр. подкл-е L2/4-C2/4 обратн. петли", "неиспр. подкл-е L1/3-C1/3 петли",
            "имитация неисправности", "внешний", "перегрузка", "нет в наличии", "неисправность заземления",
            "неиспр. подкл-е L1/3-C1/3 петли", "ошибка конфигурации", "ошибка запуска", "ошибка подключения",
            "ошибка адресации", null},
        {null, "короткое замыкание", "обрыв", null, null, "перегрузка", "нет в наличии",
            "неисправность заземления", "неиспр. подкл-е L2/4-C2/4 обратн. петли", "ошибка конфигурации",
            "ошибка запуска", null, "ошибка адресации", null},
        {null, "замковое устр-во отключено", "авт. режим отключен", "панель управления пожарной команды",
            "выход emergency откл", "огранич.", null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, "автомат.", "внешний", "панель управления пожарной команды", "не подтверждено", "общ подтверждено",
            "авт подтверждено", "панель управления пожарной команды подтверждено", null, null, null, null, null,
            null},
        {null, "панель управления пожарной команды", null, null, null, null, null, null, null, null, null, null,
            null, null},
        {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, "1-й уровень", "2-й уровень", "3-й уровень", "4-й уровень", "5-й уровень", "6-й уровень",
            "7-й уровень", null, null, null, null, null, null},
        {null, "имитация", "автомат.", null, null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, "внешний", null, null, null, null, null, null, null, null, null, null, null, null},
        {null, "автомат.", "внешний", null, null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, "имитация тревоги", "дымовой датчик-тревога", "тепловой датчик-тревога", "вскрытие", null, null,
            "тревога CO датчика", "совмещенный тепловой дымовой", "совмещенный СО дымовой",
            "совмещенный СО тепловой", "совмещенный СО тепл. дымовой", "датчик давления",
            "совмещенный тепл. давления"},
        {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, "внешний", null, null, null, null, null, null, null, null, null, null, null, null},
        {null, "имитация", null, null, null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        {null, null, "дымовой датчик-тревога", "тепловой датчик-тревога", null, null, null, "СО датчик",
            "совмещенный тепл. дымовой", "совмещенный СО дымовой", "совмещенный СО тепловой",
            "совмещенный СО тепл. дымовой", "датчик давления", "совмещенный тепл. давления"},
        {null, null, "дымовой датчик-тревога", "тепловой датчик-тревога", null, null, null, "СО датчик",
            "совмещенный тепл. дымовой", "совмещенный СО дымовой", "совмещенный СО тепловой",
            "совмещенный СО тепл. дымовой", "датчик давления", "совмещенный тепл.-давления"},
        {null, null, "дымовой датчик-тревога", "тепловой датчик-тревога", "вскрытие", null, null, "СО датчик",
            "совмещенный тепл. дымовой", "совмещенный СО дымовой", "совмещенный СО тепловой",
            "совмещенный СО тепл. дымовой", "датчик давления", "совмещенный тепл. давления"}};

    static final private String[] ELEMENTYPES = {"датчик", // 0
        "вход", // 1
        "выход", // 2
        "внешний", // 3
        "принтер", // 4
        "пульт управления", // 5
        "аккумуляторы", // 6
        "сеть IntegralLAN", // 7
        "активная часть модуля", // 8
        "пассивная часть модуля", // 9
        "задержка тревоги", // 10
        "панель пожарной бригады", // 11
        "станция ПС Schrack", // 12
        "останов \"тревоги\" оператором", // 13
        "индикатор", // 14
        "соединение LAN", // 15
        "система управления", // 16
        "шлейф", // 17
        "удаленный доступ", // 18
        "тех.обслуживание", // 19
        "направление пожаротушения", // 20
        "мастер станция", // 21
        "узел сети Seconet", // 22
        "сеть Seconet", // 23
        "контролируемый выход", // 24
        "внешний принтер", // 25
        "зона оповещения", // 26
        "внешняя система", // 27
        "панель", // 28
        "источник питания" // 29
};

    // subtypes set
    private final static String[][] SUBTYPES = {
        {"тип устр-ва по умолчанию", "автоматический датчик", "ручной извещатель", "ВПТ", "АУПТ", "АУГПТ",
            "запотолочн. датч.", "двухпороговый. датч.", "радиодатчик", "ручн. активация", "авт. активация"},
        {"тип устройства по умолчанию", "АУПТ", "неисправность АУПТ", "активация АУПТ", null, null, null, null,
            null, null, null}, // 1
        {"тип устр-ва по умолчанию", "АУПТ", "основн. аудио сигнал", "дополн. аудио сигнал", "зона оповещения",
            "вентиляция и ДУ", "противопожарное оборуд. ДУ и вент.", "активация АУПТ", null, null, null}, // 2
        {"тип устр-ва по умолчанию", "АУПТ", null, null, null, null, null, null, null, null, null}, // 3
        {"тип устр-ва по умолчанию", "принтер станции ПС", "внешний принтер", null, null, null, null, null, null,
            null, null}, // 4
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 5
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 6
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 7
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 8
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 9
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 10
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 11
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 12
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 13
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 14
        {"тип устр-ва по умолчанию", "сеть MaxNet", "система управления (ГРИФОН)", null, null, null, null, null,
            null, null, null}, // 15
        {"тип устр-ва по умолчанию", "сеть MaxNet", "система управления (ГРИФОН)", "АРМ (ГРИФОН)", null, null,
            null, null, null, null, null}, // 16
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 17
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 18
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 19
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 20
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 21
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 22
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 23
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 24
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null}, // 25
        {"тип устр-ва по умолчанию", "оповещение", null, null, null, null, null, null, null, null, null}, // 26
        {"тип устр-ва по умолчанию", "система управления (ГРИФОН)", "АРМ (ГРИФОН)",
            "удален. сервер обр-ки сообщений", "отправка email сообщения", null, null, null, null, null, null}, // 27
        {"тип устр-ва по умолчанию", null, null, null, null, null, null, null, null, null, null},// 28
    };

    static final private String[] FUNCTIONTYPES = {"изменение состояния", // 0
        "фильтр диапазона", // 1
        "фильтр сообщений", // 2
        "уровень доступа", // 3
        "внутр. акуст.", // 4
        "автоматическая активация", // 5
        "подтверждение", // 6
        "локальная операция", // 7
        "логич. состояние элемента", // 8
        "питание шлейфа", // 9
        "действие в ревизии", // 10
        "тест", // 11
        "обработка тревоги", // 12
        "питание" // 13
};

    String getDevState(int stateDev, int subStateDev) {

        if ((stateDev < 29) && (subStateDev < 14) && (stateDev >= 0) && (subStateDev >= 0)) {
            return SUBSTATES[stateDev][subStateDev];
        } else {
            return null;
        }

    }

    String getSubType(int type, int subType) {

        if ((type < 29) && (subType < 11) && (type >= 0) && (subType >= 0)) {
            return SUBTYPES[type][subType];
        } else {
            return null;
        }

    }

    String getElementType(int elementType) {

        if ((elementType < 30) && (elementType >= 0)) {
            return ELEMENTYPES[elementType];
        } else {
            return "неизвестное устройство";
        }

    }

    String getFunctionType(int funcType) {

        if ((funcType < 14) && (funcType >= 0)) {
            return FUNCTIONTYPES[funcType];
        } else {
            return "функция устройства не определена";
        }

    }

    String getState(int state) {

        return STATES[state];

    }

    String getCommand(int command) {

        return COMMANDS[command];

    }

    String getAddInfo(int addInfo) {

        if ((addInfo < 6) && (addInfo >= 0)) {
            return ADDINFO[addInfo];
        } else {
            return null;
        }

    }

}
