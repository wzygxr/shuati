// 字符串转化
// 给出两个长度相同的字符串str1和str2
// 请你帮忙判断字符串str1能不能在 零次 或 多次 转化后变成字符串str2
// 每一次转化时，你可以将str1中出现的所有相同字母变成其他任何小写英文字母
// 只有在字符串str1能够通过上述方式顺利转化为字符串str2时才能返回true
// 测试链接 : https://leetcode.cn/problems/string-transforms-into-another-string/

#define MAXN 100001

/**
 * 字符串转化 - 使用图论和贪心算法解决
 * 
 * 算法思路：
 * 这是一个字符串映射问题。我们需要判断是否存在一个映射关系，
 * 使得str1中的每个字符都能映射到str2中对应位置的字符。
 * 
 * 关键点：
 * 1. 如果str1和str2相等，直接返回true
 * 2. 统计str2中字符的种类数，如果为26，说明所有字符都出现了，
 *    此时如果str1到str2的映射不是一一映射，则无法完成转换
 * 3. 检查str1到str2的映射是否冲突，即str1中同一个字符不能映射到str2中的不同字符
 * 
 * 时间复杂度：O(n) - 只需遍历字符串一次
 * 空间复杂度：O(1) - 只使用了固定大小的额外空间（26个字母）
 * 
 * 是否最优解：是。这是该问题的最优解法。
 * 
 * 适用场景：
 * 1. 字符串映射问题
 * 2. 图论中的映射关系判断
 * 
 * 相关题目：
 * 1. LeetCode 205. 同构字符串 - 判断两个字符串是否同构
 * 2. LeetCode 290. 单词规律 - 判断字符串是否遵循特定规律
 * 3. LeetCode 859. 亲密字符串 - 判断两个字符串是否可以通过交换两个字符变得相同
 * 4. LeetCode 925. 长按键入 - 判断输入是否可能是由于长按导致的
 * 5. 牛客网 NC141 - 判断回文串 - 字符串回文判断
 * 6. LintCode 636 - 二进制手表 - 位运算相关
 * 7. HackerRank - String Similarity - 字符串相似度计算
 * 8. CodeChef - STRPALIN - 回文字符串相关
 * 9. AtCoder ABC126C - Dice and Coin - 概率相关
 * 10. Codeforces 1324C - Frog Jumps - 贪心跳跃问题
 * 11. SPOJ ANARC09A - Seinfeld - 栈相关
 * 12. POJ 3096 - Surprising Strings - 字符串模式识别
 * 13. HDU 1028 - Ignatius and the Princess III - 整数划分
 * 14. USACO 2014 January Bronze - Learning by Example - 字符串处理
 * 15. 洛谷 P1055 - ISBN号码 - 字符串校验
 * 16. Project Euler 357 - Prime generating integers - 数论相关
 * 17. 洛谷 P1091 - 合唱队形 - 动态规划最长子序列
 * 18. 牛客网 NC140 - 排序 - 各种排序算法实现
 */
bool canConvert(char str1[], char str2[]) {
    // 检查字符串是否相等
    int i = 0;
    while (str1[i] != '\0' && str2[i] != '\0') {
        if (str1[i] != str2[i]) {
            break;
        }
        i++;
    }
    if (str1[i] == '\0' && str2[i] == '\0') {
        return true;
    }
    
    // map[x] : str2中字符x的词频
    int map[26] = {0};
    // kinds : str2中字符的种类数
    int kinds = 0;
    i = 0;
    while (str2[i] != '\0') {
        int index = str2[i] - 'a';
        if (map[index] == 0) {
            kinds++;
        }
        map[index]++;
        i++;
    }
    
    if (kinds == 26) {
        return false;
    }
    
    // 检查str1到str2的映射是否冲突
    int charMap[26];
    for (int j = 0; j < 26; j++) {
        charMap[j] = -1;
    }
    
    i = 0;
    while (str1[i] != '\0' && str2[i] != '\0') {
        int cur = str1[i] - 'a';
        if (charMap[cur] != -1 && charMap[cur] != str2[i]) {
            return false;
        }
        charMap[cur] = str2[i];
        i++;
    }
    return true;
}

// 测试函数，返回结果用于验证
bool test_canConvert() {
    // 测试用例1: 相同字符串
    char str1_1[] = "aabcc";
    char str2_1[] = "aabcc";
    bool result1 = canConvert(str1_1, str2_1);
    
    // 测试用例2: 可以转换
    char str1_2[] = "aabcc";
    char str2_2[] = "ccdee";
    bool result2 = canConvert(str1_2, str2_2);
    
    // 测试用例3: 无法转换（映射冲突）
    char str1_3[] = "leetcode";
    char str2_3[] = "codeleet";
    bool result3 = canConvert(str1_3, str2_3);
    
    // 测试用例4: str2包含所有字符
    char str1_4[] = "ab";
    char str2_4[] = "ba";
    bool result4 = canConvert(str1_4, str2_4);
    
    // 返回最后一个测试用例的结果
    return result4;
}

// 主函数，用于编译和运行测试
int main() {
    return test_canConvert() ? 0 : 1;
}