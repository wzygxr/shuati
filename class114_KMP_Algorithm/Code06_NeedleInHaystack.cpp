/*
 * SPOJ NHAY - A Needle in the Haystack
 * 
 * 题目描述：
 * 编写一个程序，在给定的输入字符串中找到所有给定模式的出现位置。
 * 这通常被称为在干草堆中找针。
 * 
 * 输入格式：
 * 输入包含多个测试用例。
 * 每个测试用例由两行组成：
 * 第一行包含模式的长度m (1 <= m <= 10000)
 * 第二行包含模式本身
 * 第三行包含文本的长度n (1 <= n <= 1000000)
 * 第四行包含文本本身
 * 
 * 输出格式：
 * 对于每个测试用例，输出所有匹配位置的索引（从0开始）。
 * 如果没有匹配，则不输出任何内容。
 * 
 * 算法思路：
 * 使用KMP算法进行字符串匹配，找到所有匹配位置。
 * 
 * 时间复杂度：O(n + m)，其中n是文本串长度，m是模式串长度
 * 空间复杂度：O(m)，用于存储next数组
 */

#define MAXN 1000001
#define MAXM 10001

int next[MAXM];
int positions[MAXN];
int posCount;

/*
 * 构建KMP算法的next数组（部分匹配表）
 * 
 * next[i]表示pattern[0...i]子串的最长相等前后缀的长度
 */
void buildNextArray(char* pattern, int length) {
    // 初始化
    next[0] = 0;
    int prefixLen = 0;  // 当前最长相等前后缀的长度
    int i = 1;          // 当前处理的位置
    
    // 从位置1开始处理
    while (i < length) {
        // 如果当前字符匹配，可以延长相等前后缀
        if (pattern[i] == pattern[prefixLen]) {
            prefixLen++;
            next[i] = prefixLen;
            i++;
        } 
        // 如果不匹配且前缀长度大于0，需要回退
        else if (prefixLen > 0) {
            prefixLen = next[prefixLen - 1];
        } 
        // 如果不匹配且前缀长度为0，next[i] = 0
        else {
            next[i] = 0;
            i++;
        }
    }
}

/*
 * 在文本串中查找模式串的所有出现位置
 * 
 * @param text 文本串
 * @param textLen 文本串长度
 * @param pattern 模式串
 * @param patternLen 模式串长度
 */
void findAllOccurrences(char* text, int textLen, char* pattern, int patternLen) {
    posCount = 0;
    
    // 边界条件处理
    if (patternLen == 0 || textLen < patternLen) {
        return;
    }
    
    // 构建next数组
    buildNextArray(pattern, patternLen);
    
    int textIndex = 0;      // 文本串指针
    int patternIndex = 0;   // 模式串指针
    
    // 匹配过程
    while (textIndex < textLen) {
        // 字符匹配，两个指针都向前移动
        if (text[textIndex] == pattern[patternIndex]) {
            textIndex++;
            patternIndex++;
        } 
        // 字符不匹配且模式串指针不为0，根据next数组调整模式串指针
        else if (patternIndex > 0) {
            patternIndex = next[patternIndex - 1];
        } 
        // 字符不匹配且模式串指针为0，文本串指针向前移动
        else {
            textIndex++;
        }
        
        // 如果模式串指针等于模式串长度，说明匹配成功
        if (patternIndex == patternLen) {
            // 记录匹配位置（从0开始计数）
            positions[posCount++] = textIndex - patternIndex;
            // 根据next数组调整模式串指针，继续查找下一个匹配
            patternIndex = next[patternIndex - 1];
        }
    }
}

// 为了符合项目要求，不包含任何输出语句
// 实际使用时可以根据需要添加适当的输出代码
int main() {
    // 测试用例1
    char text1[] = "abcabcabcabc";
    char pattern1[] = "abc";
    findAllOccurrences(text1, 12, pattern1, 3);
    
    // 测试用例2
    char text2[] = "abababab";
    char pattern2[] = "aba";
    findAllOccurrences(text2, 8, pattern2, 3);
    
    // 测试用例3
    char text3[] = "aaaaa";
    char pattern3[] = "aa";
    findAllOccurrences(text3, 5, pattern3, 2);
    
    // 测试用例4 - 无匹配
    char text4[] = "abcdef";
    char pattern4[] = "xyz";
    findAllOccurrences(text4, 6, pattern4, 3);
    
    return 0;
}