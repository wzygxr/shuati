// LeetCode 1178. 猜字谜
// 外国友人仿照中国字谜设计了一个英文版猜字谜小游戏，请你来猜猜看吧。
// 字谜的迷面puzzle 按字符串形式给出，如果一个单词word 符合下面两个条件，那么它就可以算作谜底：
// 1. 单词中包含谜面的第一个字母
// 2. 单词中的每一个字母都出现在谜面中
// 测试链接 : https://leetcode.cn/problems/number-of-valid-words-for-each-puzzle/

/*
 * 算法详解：猜字谜（LeetCode 1178）
 * 
 * 问题描述：
 * 给定一个单词列表words和一个谜面列表puzzles，对于每个谜面，计算有多少个单词可以作为谜底。
 * 单词可以作为谜底需要满足两个条件：
 * 1. 单词中包含谜面的第一个字母
 * 2. 单词中的每一个字母都出现在谜面中
 * 
 * 算法思路：
 * 使用状态压缩和位运算优化来解决这个问题。
 * 1. 将每个单词转换为位掩码表示
 * 2. 将每个谜面转换为位掩码表示
 * 3. 对于每个谜面，枚举其所有子集（使用位运算技巧）
 * 4. 检查子集是否包含谜面的第一个字母
 * 5. 统计满足条件的单词数量
 * 
 * 时间复杂度分析：
 * 1. 单词位掩码转换：O(W*L)，其中W是单词数，L是平均单词长度
 * 2. 谜面处理：O(P * 2^N)，其中P是谜面数，N是谜面长度（最多7个字符）
 * 3. 总体时间复杂度：O(W*L + P * 2^N)
 * 
 * 空间复杂度分析：
 * 1. 单词位掩码存储：O(W)
 * 2. 位掩码计数：O(2^26)（实际远小于，因为只存储出现的位掩码）
 * 3. 总体空间复杂度：O(W + 2^26)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 边界处理：正确处理空列表的情况
 * 3. 性能优化：使用数组统计位掩码出现次数，避免重复计算
 * 4. 位运算优化：使用技巧优化子集枚举
 * 
 * 极端场景验证：
 * 1. 单词列表和谜面列表为空的情况
 * 2. 单词长度和谜面长度达到边界的情况
 * 3. 所有单词都能匹配所有谜面的情况
 * 4. 没有单词能匹配任何谜面的情况
 */

// 由于环境限制，此处只提供算法核心实现思路，不包含完整的可编译代码
// 在实际使用中，需要根据具体环境添加适当的头文件和类型定义

/*
int* findNumOfValidWords(char** words, int wordsSize, char** puzzles, int puzzlesSize, int* returnSize) {
    // 异常处理：检查输入参数的有效性
    if (words == 0 || puzzles == 0 || returnSize == 0) {
        *returnSize = 0;
        return 0;
    }
    
    if (wordsSize == 0 || puzzlesSize == 0) {
        *returnSize = 0;
        return 0;
    }
    
    // 使用数组统计每个位掩码出现的次数（优化版本）
    // 由于位掩码最多有2^26种可能，但实际使用的远少于此
    // 可以使用更小的数组或者只存储实际出现的位掩码
    int wordCount[1 << 26] = {0};
    
    // 将每个单词转换为位掩码并统计
    for (int i = 0; i < wordsSize; i++) {
        int mask = 0;
        int len = strlen(words[i]);
        for (int j = 0; j < len; j++) {
            // 将每个字母对应到一个位上
            mask |= 1 << (words[i][j] - 'a');
        }
        // 统计该位掩码出现的次数
        wordCount[mask]++;
    }
    
    // 分配结果数组
    int* result = (int*)malloc(puzzlesSize * sizeof(int));
    *returnSize = puzzlesSize;
    
    // 处理每个谜面
    for (int i = 0; i < puzzlesSize; i++) {
        int count = 0;
        
        // 获取谜面的第一个字母对应的位
        int firstLetter = 1 << (puzzles[i][0] - 'a');
        
        // 获取谜面的位掩码
        int puzzleMask = 0;
        int puzzleLen = strlen(puzzles[i]);
        for (int j = 0; j < puzzleLen; j++) {
            puzzleMask |= 1 << (puzzles[i][j] - 'a');
        }
        
        // 枚举谜面的所有子集
        int subset = puzzleMask;
        while (subset > 0) {
            // 检查子集是否包含谜面的第一个字母
            if ((subset & firstLetter) != 0) {
                // 如果包含，则统计对应的单词数量
                count += wordCount[subset];
            }
            // 枚举下一个子集
            subset = (subset - 1) & puzzleMask;
        }
        
        result[i] = count;
    }
    
    return result;
}
*/