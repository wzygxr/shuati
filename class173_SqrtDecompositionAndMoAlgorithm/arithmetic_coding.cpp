/**
 * 算术编码实现 (C++纯算法版本)
 * 
 * 算术编码是一种无损数据压缩方法，它将整个输入消息编码为一个位于[0,1)区间内的实数。
 * 
 * 算法原理：
 * 1. 统计字符频率，构建概率模型
 * 2. 根据概率模型构建累积分布函数(CDF)
 * 3. 对输入字符串进行编码，将整个字符串映射到[0,1)区间的一个子区间
 * 4. 解码时根据相同的概率模型和编码值还原原始字符串
 * 
 * 时间复杂度：
 * - 编码：O(n)，其中n是输入字符串长度
 * - 解码：O(n)，其中n是输出字符串长度
 * 
 * 空间复杂度：O(k)，其中k是不同字符的数量
 * 
 * 优势：
 * 1. 压缩率高，可以达到信息熵的理论极限
 * 2. 可以处理任意精度的概率
 * 3. 适合处理具有明显统计特性的数据
 * 
 * 劣势：
 * 1. 实现复杂，需要处理浮点数精度问题
 * 2. 编码和解码必须使用相同的概率模型
 * 3. 对于短字符串，可能不如其他简单编码方法高效
 * 
 * 应用场景：
 * 1. 图像压缩（JPEG）
 * 2. 音频压缩
 * 3. 数据压缩标准
 */

// 算术编码器结构
#define MAX_CHARS 256

// 编码结果结构
typedef struct {
    double low;
    double high;
} CodeResult;

// 算术编码器
typedef struct {
    int frequency[MAX_CHARS];      // 字符频率
    int cumulativeFreq[MAX_CHARS]; // 累积频率
    int totalFreq;                 // 总频率
    int charCount;                 // 字符种类数
    char chars[MAX_CHARS];         // 字符列表
} ArithmeticCoding;

/**
 * 初始化算术编码器
 */
void initArithmeticCoding(ArithmeticCoding* ac) {
    // 初始化所有频率为0
    for (int i = 0; i < MAX_CHARS; i++) {
        ac->frequency[i] = 0;
        ac->cumulativeFreq[i] = 0;
    }
    ac->totalFreq = 0;
    ac->charCount = 0;
}

/**
 * 统计字符频率
 */
void buildFrequencyMap(ArithmeticCoding* ac, const char* input) {
    // 重置频率统计
    for (int i = 0; i < MAX_CHARS; i++) {
        ac->frequency[i] = 0;
    }
    
    // 统计字符频率
    int len = 0;
    while (input[len] != '\0') {
        ac->frequency[(unsigned char)input[len]]++;
        len++;
    }
    
    // 添加EOF字符
    ac->frequency[0] = 1;
}

/**
 * 构建累积分布函数
 */
void buildCumulativeFrequency(ArithmeticCoding* ac) {
    ac->totalFreq = 0;
    ac->charCount = 0;
    
    // 按字符顺序构建累积频率
    for (int i = 0; i < MAX_CHARS; i++) {
        if (ac->frequency[i] > 0) {
            ac->chars[ac->charCount] = (char)i;
            ac->cumulativeFreq[ac->charCount] = ac->totalFreq;
            ac->totalFreq += ac->frequency[i];
            ac->charCount++;
        }
    }
}

/**
 * 根据输入字符串初始化算术编码器
 */
void initArithmeticCodingFromString(ArithmeticCoding* ac, const char* input) {
    initArithmeticCoding(ac);
    buildFrequencyMap(ac, input);
    buildCumulativeFrequency(ac);
}

/**
 * 算术编码
 */
CodeResult encode(ArithmeticCoding* ac, const char* input) {
    CodeResult result;
    result.low = 0.0;
    result.high = 1.0;
    
    // 计算输入长度
    int len = 0;
    while (input[len] != '\0') {
        len++;
    }
    
    // 对每个字符进行编码
    for (int i = 0; i <= len; i++) {  // 包括EOF字符
        char c = (i < len) ? input[i] : '\0';  // 最后一个字符是EOF
        
        // 计算当前区间的范围
        double range = result.high - result.low;
        
        // 查找字符在累积频率中的位置
        int charIndex = -1;
        for (int j = 0; j < ac->charCount; j++) {
            if (ac->chars[j] == c) {
                charIndex = j;
                break;
            }
        }
        
        if (charIndex != -1) {
            // 获取字符的概率区间
            int symbolLow = ac->cumulativeFreq[charIndex];
            int symbolHigh = symbolLow + ac->frequency[(unsigned char)c];
            
            // 缩小区间
            result.high = result.low + range * symbolHigh / ac->totalFreq;
            result.low = result.low + range * symbolLow / ac->totalFreq;
        }
    }
    
    return result;
}

/**
 * 查找字符索引
 */
int findCharIndex(ArithmeticCoding* ac, double value, double low, double high) {
    double range = high - low;
    
    for (int i = 0; i < ac->charCount; i++) {
        int symbolLow = ac->cumulativeFreq[i];
        int symbolHigh = symbolLow + ac->frequency[(unsigned char)ac->chars[i]];
        
        double symbolLowValue = low + range * symbolLow / ac->totalFreq;
        double symbolHighValue = low + range * symbolHigh / ac->totalFreq;
        
        if (value >= symbolLowValue && value < symbolHighValue) {
            return i;
        }
    }
    
    return -1;
}

/**
 * 算术解码
 */
void decode(ArithmeticCoding* ac, CodeResult code, int maxLength, char* output) {
    double value = (code.low + code.high) / 2;  // 使用区间的中点作为解码值
    double low = 0.0;
    double high = 1.0;
    
    int outputIndex = 0;
    
    while (outputIndex < maxLength - 1) {
        double range = high - low;
        
        // 查找对应的字符
        int charIndex = findCharIndex(ac, value, low, high);
        
        if (charIndex == -1) {
            break;
        }
        
        char foundChar = ac->chars[charIndex];
        
        // 如果是EOF字符，结束解码
        if (foundChar == '\0') {
            break;
        }
        
        // 更新区间
        int symbolLow = ac->cumulativeFreq[charIndex];
        int symbolHigh = symbolLow + ac->frequency[(unsigned char)foundChar];
        
        double symbolLowValue = low + range * symbolLow / ac->totalFreq;
        double symbolHighValue = low + range * symbolHigh / ac->totalFreq;
        
        low = symbolLowValue;
        high = symbolHighValue;
        
        output[outputIndex++] = foundChar;
    }
    
    output[outputIndex] = '\0';  // 添加字符串结束符
}

/**
 * 获取字符频率（用于调试）
 */
int getFrequency(ArithmeticCoding* ac, char c) {
    return ac->frequency[(unsigned char)c];
}

/**
 * 获取总频率
 */
int getTotalFrequency(ArithmeticCoding* ac) {
    return ac->totalFreq;
}

/**
 * 获取字符种类数
 */
int getCharCount(ArithmeticCoding* ac) {
    return ac->charCount;
}

// 由于环境限制，不包含main函数和输出语句
// 算法核心功能已实现，可被其他程序调用