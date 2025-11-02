/**
 * LZW字典编码实现 (C++简化版本)
 * 
 * LZW（Lempel-Ziv-Welch）是一种无损数据压缩算法，属于字典编码的一种。
 * 
 * 算法原理：
 * 1. 初始化字典，包含所有可能的单字符
 * 2. 读取输入字符串，查找字典中最长的匹配字符串
 * 3. 输出匹配字符串对应的编码
 * 4. 将匹配字符串加上下一个字符组成的新字符串添加到字典中
 * 5. 重复步骤2-4直到处理完所有输入
 * 
 * 时间复杂度：O(n)，其中n是输入字符串长度
 * 空间复杂度：O(d)，其中d是字典中条目的数量
 * 
 * 优势：
 * 1. 实现相对简单
 * 2. 压缩效果好，特别适合重复模式较多的数据
 * 3. 不需要预先知道数据的统计特性
 * 4. 编码和解码过程对称
 * 
 * 劣势：
 * 1. 需要维护字典，占用内存
 * 2. 对于随机数据压缩效果不佳
 * 3. 字典可能会变得很大
 * 
 * 应用场景：
 * 1. GIF图像格式
 * 2. TIFF图像格式
 * 3. Unix系统的compress工具
 */

// 定义最大字典大小
#define MAX_DICTIONARY_SIZE 4096
#define MAX_STRING_LENGTH 100

// 字符串结构
typedef struct {
    char str[MAX_STRING_LENGTH];
    int length;
} LZWString;

// 字典条目结构
typedef struct {
    LZWString key;
    int value;
} DictionaryEntry;

// 字典结构
typedef struct {
    DictionaryEntry entries[MAX_DICTIONARY_SIZE];
    int size;
} Dictionary;

// 整数向量结构
typedef struct {
    int data[MAX_DICTIONARY_SIZE];
    int size;
} IntVector;

/**
 * 初始化字符串
 */
void initString(LZWString* str, const char* chars, int len) {
    for (int i = 0; i < len && i < MAX_STRING_LENGTH - 1; i++) {
        str->str[i] = chars[i];
    }
    str->str[len] = '\0';
    str->length = len;
}

/**
 * 复制字符串
 */
void copyString(LZWString* dest, const LZWString* src) {
    for (int i = 0; i <= src->length && i < MAX_STRING_LENGTH; i++) {
        dest->str[i] = src->str[i];
    }
    dest->length = src->length;
}

/**
 * 连接字符到字符串
 */
void appendStringChar(LZWString* str, char c) {
    if (str->length < MAX_STRING_LENGTH - 1) {
        str->str[str->length] = c;
        str->str[str->length + 1] = '\0';
        str->length++;
    }
}

/**
 * 比较两个字符串
 */
int compareString(const LZWString* str1, const LZWString* str2) {
    if (str1->length != str2->length) {
        return 0;
    }
    for (int i = 0; i < str1->length; i++) {
        if (str1->str[i] != str2->str[i]) {
            return 0;
        }
    }
    return 1;
}

/**
 * 初始化字典
 */
void initDictionary(Dictionary* dict) {
    dict->size = 0;
}

/**
 * 在字典中查找字符串
 */
int findInDictionary(Dictionary* dict, const LZWString* key) {
    for (int i = 0; i < dict->size; i++) {
        if (compareString(&dict->entries[i].key, key)) {
            return dict->entries[i].value;
        }
    }
    return -1; // 未找到
}

/**
 * 向字典添加条目
 */
void addToDictionary(Dictionary* dict, const LZWString* key, int value) {
    if (dict->size < MAX_DICTIONARY_SIZE) {
        copyString(&dict->entries[dict->size].key, key);
        dict->entries[dict->size].value = value;
        dict->size++;
    }
}

/**
 * 从字典获取字符串
 */
int getFromDictionary(Dictionary* dict, int key, LZWString* result) {
    for (int i = 0; i < dict->size; i++) {
        if (dict->entries[i].value == key) {
            copyString(result, &dict->entries[i].key);
            return 1; // 成功
        }
    }
    return 0; // 未找到
}

/**
 * 初始化整数向量
 */
void initIntVector(IntVector* vec) {
    vec->size = 0;
}

/**
 * 向整数向量添加元素
 */
void addToIntVector(IntVector* vec, int value) {
    if (vec->size < MAX_DICTIONARY_SIZE) {
        vec->data[vec->size] = value;
        vec->size++;
    }
}

/**
 * LZW编码
 */
IntVector encode(const char* input) {
    Dictionary dictionary;
    initDictionary(&dictionary);
    
    // 初始化字典，包含所有ASCII字符
    for (int i = 0; i < 256; i++) {
        LZWString key;
        char ch = (char)i;
        initString(&key, &ch, 1);
        addToDictionary(&dictionary, &key, i);
    }
    
    IntVector result;
    initIntVector(&result);
    
    LZWString current;
    initString(&current, "", 0);
    
    int i = 0;
    while (input[i] != '\0') {
        char c = input[i];
        LZWString combined;
        copyString(&combined, &current);
        appendStringChar(&combined, c);
        
        // 如果组合字符串在字典中，继续扩展
        if (findInDictionary(&dictionary, &combined) != -1) {
            copyString(&current, &combined);
        } else {
            // 输出当前字符串的编码
            int code = findInDictionary(&dictionary, &current);
            if (code != -1) {
                addToIntVector(&result, code);
            }
            
            // 将新字符串添加到字典
            addToDictionary(&dictionary, &combined, 256 + dictionary.size);
            
            // 重新开始
            initString(&current, &c, 1);
        }
        i++;
    }
    
    // 输出最后一个字符串
    if (current.length > 0) {
        int code = findInDictionary(&dictionary, &current);
        if (code != -1) {
            addToIntVector(&result, code);
        }
    }
    
    return result;
}

/**
 * LZW解码
 */
void decode(IntVector* encoded, char* output) {
    Dictionary dictionary;
    initDictionary(&dictionary);
    
    // 初始化字典，包含所有ASCII字符
    for (int i = 0; i < 256; i++) {
        LZWString key;
        char ch = (char)i;
        initString(&key, &ch, 1);
        addToDictionary(&dictionary, &key, i);
    }
    
    output[0] = '\0';
    LZWString current;
    initString(&current, "", 0);
    
    for (int i = 0; i < encoded->size; i++) {
        int code = encoded->data[i];
        LZWString entry;
        
        if (getFromDictionary(&dictionary, code, &entry)) {
            // 成功获取条目
        } else if (code == 256 + dictionary.size) {
            // 特殊情况：处理字符串+首字符的重复情况
            copyString(&entry, &current);
            if (current.length > 0) {
                appendStringChar(&entry, current.str[0]);
            }
        } else {
            // 无效编码
            output[0] = '\0';
            return;
        }
        
        // 将条目添加到输出
        int outputLen = 0;
        while (output[outputLen] != '\0') {
            outputLen++;
        }
        
        for (int j = 0; j < entry.length && outputLen + j < MAX_DICTIONARY_SIZE - 1; j++) {
            output[outputLen + j] = entry.str[j];
        }
        output[outputLen + entry.length] = '\0';
        
        // 将新字符串添加到字典
        if (current.length > 0) {
            LZWString newEntry;
            copyString(&newEntry, &current);
            if (entry.length > 0) {
                appendStringChar(&newEntry, entry.str[0]);
            }
            addToDictionary(&dictionary, &newEntry, 256 + dictionary.size);
        }
        
        copyString(&current, &entry);
    }
}

/**
 * 计算压缩率
 */
double calculateCompressionRatio(int original, int compressed) {
    if (original == 0) return 0;
    return (1.0 - (double) compressed / original) * 100;
}

// 由于环境限制，不包含main函数和输出语句
// 算法核心功能已实现，可被其他程序调用