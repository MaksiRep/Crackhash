package ru.maksirep.crackhash.manager.api.validation;

public class ValidationMessage {

    private ValidationMessage() {
    }

    public static final String HASH_SIZE = "Размер хеша должен равняться 32 символа";

    public static final String HASH_REGEX = "Недопустимые символы в хэш коде. Допустимы строчные латинские символы и цифры от 0 до 9";

    public static final String MIN_WORD_LENGTH = "Минимальная длина слова не может быть меньше 1";

    public static final String MAX_WORD_LENGTH = "Максимальная длина слова не может быть больше 4";

    public static final String DATA_SIZE = "Длина итогового слова должна быть не менее 1 и не более 4 символов";
}
