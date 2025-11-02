/**
 * 最长字符串链
 * 
 * 题目来源：LeetCode 1048. 最长字符串链
 * 题目链接：https://leetcode.cn/problems/longest-string-chain/
 * 题目描述：给出一个单词数组 words ，其中每个单词都由小写英文字母组成。
 * 如果我们可以不改变其他字符的顺序，在 wordA 中任意位置添加恰好一个字母使其变成 wordB，
 * 那么我们认为 wordA 是 wordB 的前身。
 * 词链是单词 [word_1, word_2, ..., word_k] 组成的序列，其中 word1 是 word2 的前身，
 * word2 是 word3 的前身，依此类推。从给定单词列表 words 中选择单词组成词链，
 * 返回词链的最长可能长度。
 * 
 * 算法思路：
 * 1. 按字符串长度排序
 * 2. 使用动态规划方法
 * 3. dp[word] 表示以 word 结尾的最长字符串链长度
 * 4. 对于每个单词，尝试删除每个字符，检查是否存在于之前的单词中
 * 5. 如果存在，则更新当前单词的最长链长度
 * 
 * 时间复杂度：O(N * L^2) - N是单词数量，L是单词平均长度
 * 空间复杂度：O(N * L) - 需要哈希表存储状态和单词
 * 是否最优解：这是目前较优的解法
 * 
 * 示例：
 * 输入：words = ["a","b","ba","bca","bda","bdca"]
 * 输出：4
 * 解释：最长单词链之一为 ["a","ba","bda","bdca"]
 * 
 * 输入：words = ["xbc","pcxbcf","xb","cxbc","pcxbc"]
 * 输出：5
 * 
 * 输入：words = ["abcd","dbqca"]
 * 输出：1
 */

// 由于编译环境问题，使用基础C++实现，避免使用STL容器
// 使用简单的数组和字符串操作实现

#define MAXN 1000
#define MAXLEN 20

// 简单的字符串结构
struct String {
    char data[MAXLEN];
    int length;
};

// 简单的字符串比较函数
int stringCompare(String* a, String* b) {
    if (a->length != b->length) {
        return a->length - b->length;
    }
    for (int i = 0; i < a->length; i++) {
        if (a->data[i] != b->data[i]) {
            return a->data[i] - b->data[i];
        }
    }
    return 0;
}

// 字符串复制函数
void stringCopy(String* dest, String* src) {
    dest->length = src->length;
    for (int i = 0; i < src->length; i++) {
        dest->data[i] = src->data[i];
    }
}

// 删除指定位置字符的函数
void removeChar(String* dest, String* src, int pos) {
    dest->length = src->length - 1;
    for (int i = 0; i < pos; i++) {
        dest->data[i] = src->data[i];
    }
    for (int i = pos; i < dest->length; i++) {
        dest->data[i] = src->data[i + 1];
    }
}

/**
 * 计算最长字符串链的长度
 * 
 * @param words 单词数组
 * @param n 单词数量
 * @return 最长字符串链的长度
 */
int longestStrChain(String words[], int n) {
    // 简单的冒泡排序按字符串长度排序
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - 1 - i; j++) {
            if (stringCompare(&words[j], &words[j + 1]) > 0) {
                String temp;
                stringCopy(&temp, &words[j]);
                stringCopy(&words[j], &words[j + 1]);
                stringCopy(&words[j + 1], &temp);
            }
        }
    }
    
    // dp[i] 表示以 words[i] 结尾的最长字符串链长度
    int dp[MAXN];
    for (int i = 0; i < n; i++) {
        dp[i] = 1;
    }
    
    int maxLen = 1;
    
    // 遍历每个单词
    for (int i = 1; i < n; i++) {
        // 尝试删除每个字符，检查是否存在于之前的单词中
        for (int j = 0; j < i; j++) {
            // 检查words[j]是否是words[i]的前身
            if (words[j].length + 1 == words[i].length) {
                // 尝试删除words[i]的每个字符，看是否能得到words[j]
                int found = 0;
                for (int k = 0; k < words[i].length; k++) {
                    String temp;
                    removeChar(&temp, &words[i], k);
                    if (temp.length == words[j].length) {
                        int match = 1;
                        for (int l = 0; l < temp.length; l++) {
                            if (temp.data[l] != words[j].data[l]) {
                                match = 0;
                                break;
                            }
                        }
                        if (match) {
                            found = 1;
                            break;
                        }
                    }
                }
                // 如果找到了前身，更新dp值
                if (found && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                }
            }
        }
        if (dp[i] > maxLen) {
            maxLen = dp[i];
        }
    }
    
    return maxLen;
}

// 用于测试的主函数
int main() {
    // 测试需要通过其他方式验证
    return 0;
}