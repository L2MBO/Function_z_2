fun main() {
    // Программа шифрования методом Вижинера

    print("Введите сообщение: ")
    val msg = readln()

    print("Введите ключ: ")
    val key = readln()

    print("Использовать типовую таблицу? (д/н) ")
    val type = readln()

    val table = if (type.lowercase() == "д") {
        genDefaultTable() // Генерация типовой таблицы
    } else if (type.lowercase() == "н") { //
        genRandomTable() // Генерация случайной таблицы
    } else {
        println("Неверный ввод!")
        return
    }

    print("Действие [1-шифр, 2-дешифр]: ")
    val action = readln()

    val result = when(action){
        "1" -> encrypt(msg,key,table)
        "2" -> decrypt(msg,key,table)
        else -> "Неверный ввод"
    }
    println("\nИтог: $result")
}

fun genDefaultTable(): Array<CharArray> {
    val alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".toCharArray()
    val table = Array(alphabet.size) { CharArray(alphabet.size) } // Создание двумерного массива для таблицы (размер алфавита x размер алфавита)

    for(i in alphabet.indices){ // Цикл по строкам таблицы
        for(j in alphabet.indices){ // Цикл по столбцам таблицы
            table[i][j] = alphabet[(i+j)%alphabet.size] // Заполнение таблицы по правилу сдвига Цезаря
        }
    }
    return table // Возвращение сгенерированной таблицы
}

fun genRandomTable(): Array<CharArray> {
    val alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".toCharArray()
    val table = Array(alphabet.size) { CharArray(alphabet.size) } // Создание двумерного массива
    val shuffledAlphabet = alphabet.toMutableList().shuffled().toCharArray() // Перемешивание алфавита
    val shifts = (0 until alphabet.size).shuffled().toIntArray() // Генерация случайных сдвигов для каждой строки

    for(i in alphabet.indices){ // Цикл по строкам
        for(j in alphabet.indices){ // Цикл по столбцам
            table[i][j] = shuffledAlphabet[(i + shifts[i] + j) % alphabet.size] // Заполнение таблицы со случайным сдвигом для каждой строки
        }
    }
    return table // Возвращение сгенерированной таблицы
}

fun encrypt(msg: String, key: String, table: Array<CharArray>): String{
    val alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".toCharArray() // Русский алфавит
    val alphabetIndex = alphabet.mapIndexed { index, c -> c to index }.toMap() // Создание Map для быстрого поиска индекса символа
    var encryptMsg = "" // Строка для зашифрованного сообщения
    var keyId = 0 // Индекс текущего символа ключа

    for (i in msg.indices){ // Цикл по символам сообщения
        val char = msg[i].uppercaseChar() // Текущий символ сообщения в верхнем регистре
        val keyChar = key[keyId].uppercaseChar() // Текущий символ ключа в верхнем регистре

        encryptMsg += if(char.isLetter()){ // Проверка на букву
            table[alphabetIndex.getValue(keyChar)][alphabetIndex.getValue(char)] // Шифрование символа с использованием таблицы
        } else char // Если не буква, то символ добавляется без изменений
        keyId = (keyId + 1) % key.length // Переход к следующему символу ключа (циклически)
    }
    return encryptMsg // Возвращение зашифрованного сообщения
}

fun decrypt(msg: String, key: String, table: Array<CharArray>): String{
    val alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".toCharArray() // Русский алфавит
    val alphabetIndex = alphabet.mapIndexed { index, c -> c to index }.toMap() // Map для быстрого поиска индекса, она создает пару из символа и его индекса
    var decryptMsg = "" // Строка для расшифрованного сообщения
    var keyId = 0 // Индекс текущего символа ключа

    for(i in msg.indices) { // Цикл по символам сообщения
        val char = msg[i].uppercaseChar() // Текущий символ в верхнем регистре
        val keyChar = key[keyId].uppercaseChar() // Текущий символ ключа в верхнем регистре

        decryptMsg += if(char.isLetter()){ // Проверка на букву
            alphabet[table[alphabetIndex.getValue(keyChar)].indexOf(char)] // Расшифровка символа
        } else char // Если не буква, то символ добавляется без изменений
        keyId = (keyId + 1) % key.length // Переход к следующему символу ключа (циклически)
    }
    return decryptMsg // Возвращение расшифрованного сообщения
}
