// 最短超级串 (Shortest Superstring)
// 题目来源: LeetCode 943. Find the Shortest Superstring
// 题目链接: https://leetcode.cn/problems/find-the-shortest-superstring/
// 题目描述:
// 给定一个字符串数组 words ，找到以 words 中每个字符串作为子字符串的最短字符串。
// 如果有多个有效最短字符串满足题目条件，返回其中 任意一个 即可。
// 我们可以假设 words 中没有字符串是 words 中另一个字符串的子字符串。
//
// 解题思路:
// 这是一个经典的旅行商问题(TSP)变种，可以使用状态压缩DP解决。
// 1. 预处理计算重叠部分 overlap[i][j] 表示字符串words[i]的尾部与字符串words[j]的头部的最大重叠长度
// 2. dp[mask][i] 表示使用mask代表的字符串集合，且最后一个字符串是words[i]时的最短超级字符串
// 3. 状态转移: 对于每个状态，尝试添加一个新的字符串
//
// 时间复杂度: O(n^2 * 2^n + n * sum(len))
// 空间复杂度: O(n * 2^n)
// 其中n是字符串的数量，sum(len)是所有字符串长度之和
//
// 补充题目1: 最小必要团队 (Smallest Sufficient Team)
// 题目来源: LeetCode 1125. Smallest Sufficient Team
// 题目链接: https://leetcode.cn/problems/smallest-sufficient-team/
// 题目描述:
// 给定一个人数组和一个技能需求列表，找出最小的团队使得团队成员掌握的技能能够覆盖所有需求技能。
// 解题思路:
// 1. 状态压缩动态规划解法
// 2. 建立技能到索引的映射，便于位运算
// 3. 将每个人掌握的技能转换为位掩码表示
// 4. dp[mask] 表示覆盖技能集合mask所需的最小团队，使用List存储团队成员索引
// 时间复杂度: O(2^m * n) 其中m是需求技能数，n是人员数
// 空间复杂度: O(2^m)

// 常量定义
const int MAXN = 15;        // 最大字符串数量
const int MAX_STATES = 1 << 12; // 最大状态数，2^12 = 4096
const int INF = 0x3f3f3f3f;     // 无穷大常量

// 计算字符串长度
// 参数说明:
// str: 输入的字符串
// 返回值: 字符串的长度
int strlen(const char* str) {
    int len = 0;
    while (str[len] != '\0') {
        len++;
    }
    return len;
}

// 字符串比较
// 参数说明:
// str1: 第一个字符串
// str2: 第二个字符串
// len: 比较的长度
// 返回值: 如果两个字符串的前len个字符相同返回true，否则返回false
bool strcmp(const char* str1, const char* str2, int len) {
    for (int i = 0; i < len; i++) {
        if (str1[i] != str2[i]) {
            return false;
        }
    }
    return true;
}

// 字符串复制
// 参数说明:
// dest: 目标字符串
// src: 源字符串
void strcpy(char* dest, const char* src) {
    int i = 0;
    while (src[i] != '\0') {
        dest[i] = src[i];
        i++;
    }
    dest[i] = '\0';
}

// 字符串连接
// 参数说明:
// dest: 目标字符串
// src: 源字符串
void strcat(char* dest, const char* src) {
    int destLen = strlen(dest);
    int i = 0;
    while (src[i] != '\0') {
        dest[destLen + i] = src[i];
        i++;
    }
    dest[destLen + i] = '\0';
}

// 计算字符串a的尾部与字符串b的头部的最大重叠长度
// 参数说明:
// a: 第一个字符串
// b: 第二个字符串
// 返回值: 字符串a的尾部与字符串b的头部的最大重叠长度
int getOverlap(const char* a, const char* b) {
    int lenA = strlen(a);
    int lenB = strlen(b);
    // 重叠长度最大为两个字符串长度的较小值
    int maxOverlap = lenA < lenB ? lenA : lenB;
    
    // 从最大可能的重叠长度开始向下枚举
    for (int i = maxOverlap; i >= 0; i--) {
        // 检查a的后i个字符是否与b的前i个字符相同
        if (strcmp(a + lenA - i, b, i)) {
            return i;
        }
    }
    return 0;
}

// LeetCode 943 最短超级串解法
// 参数说明:
// n: 字符串数组的长度
// words: 字符串数组
// result: 存储结果的字符串
void shortestSuperstring(int n, const char* words[], char* result) {
    // 预处理计算重叠部分
    // overlap[i][j] 表示字符串words[i]的尾部与字符串words[j]的头部的最大重叠长度
    int overlap[MAXN][MAXN];
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (i != j) {
                overlap[i][j] = getOverlap(words[i], words[j]);
            } else {
                overlap[i][j] = 0;
            }
        }
    }
    
    // dp[mask][i] 表示使用mask代表的字符串集合，且最后一个字符串是words[i]时的最短超级字符串长度
    // parent[mask][i] 用于回溯路径，记录前一个字符串的索引
    int dp[MAX_STATES][MAXN];
    int parent[MAX_STATES][MAXN];
    
    // 初始化DP数组
    for (int i = 0; i < (1 << n); i++) {
        for (int j = 0; j < n; j++) {
            dp[i][j] = INF;
        }
    }
    
    // 初始化：只包含一个字符串的情况
    // 1 << i 表示只包含第i个字符串的状态
    for (int i = 0; i < n; i++) {
        dp[1 << i][i] = strlen(words[i]);
    }
    
    // 状态转移过程
    for (int mask = 1; mask < (1 << n); mask++) {
        // 枚举当前状态下的最后一个字符串
        for (int last = 0; last < n; last++) {
            // 如果第last个字符串不在当前状态中，跳过
            if ((mask & (1 << last)) == 0) {
                continue;
            }
            
            // 如果当前状态不可达，跳过
            if (dp[mask][last] == INF) {
                continue;
            }
            
            // 尝试添加一个新的字符串
            for (int next = 0; next < n; next++) {
                // 如果第next个字符串已经在当前状态中，跳过
                if ((mask & (1 << next)) != 0) {
                    continue;
                }
                
                // 计算添加next字符串后的新状态
                int newMask = mask | (1 << next);
                // 计算新状态下的超级字符串长度
                int newLength = dp[mask][last] + strlen(words[next]) - overlap[last][next];
                
                // 如果通过当前路径能得到更短的超级字符串，更新状态
                if (dp[newMask][next] > newLength) {
                    dp[newMask][next] = newLength;
                    parent[newMask][next] = last;
                }
            }
        }
    }
    
    // 找到包含所有字符串的最短超级字符串
    int resultLength = INF;
    int lastWord = -1;
    // 枚举所有字符串作为最后一个字符串的情况
    for (int i = 0; i < n; i++) {
        // (1 << n) - 1 表示包含所有字符串的状态
        if (dp[(1 << n) - 1][i] < resultLength) {
            resultLength = dp[(1 << n) - 1][i];
            lastWord = i;
        }
    }
    
    // 回溯构建结果字符串
    // path数组存储构建超级字符串时的字符串顺序
    int path[MAXN];
    int mask = (1 << n) - 1;  // 包含所有字符串的状态
    int idx = n - 1;          // path数组的索引
    
    // 从后往前回溯，构建字符串顺序
    while (mask > 0) {
        path[idx--] = lastWord;
        int prev = parent[mask][lastWord];
        mask ^= (1 << lastWord);  // 从状态中移除lastWord字符串
        lastWord = prev;
    }
    
    // 构建最终字符串
    idx++; // 调整索引到正确位置
    strcpy(result, words[path[idx]]);  // 复制第一个字符串
    
    // 依次连接后续字符串，注意重叠部分只需要复制一次
    for (int i = idx + 1; i < n; i++) {
        int overlapLen = overlap[path[i - 1]][path[i]];
        strcat(result, words[path[i]] + overlapLen);  // 从重叠部分之后开始复制
    }
}

// LeetCode 1125 最小必要团队解法
// 参数说明:
// m: 需求技能数
// req_skills: 需求技能数组
// n: 人员数
// people: 人员技能数组，people[i]表示第i个人掌握的技能
// peopleSkillsCount: 每个人掌握的技能数量
// result: 存储结果的数组，表示最小团队的人员索引
// resultSize: 输出参数，表示最小团队的人员数量
void smallestSufficientTeam(int m, const char* req_skills[], int n, const char* people[][MAXN], int peopleSkillsCount[], int* result, int* resultSize) {
    // 建立技能到索引的映射，便于位运算
    // 这里使用简单的哈希映射，实际应用中需要更复杂的哈希函数
    int skillIndex[100]; // 假设最多100个不同的字符
    for (int i = 0; i < m; i++) {
        // 简单的哈希映射，使用技能名称的第一个字符作为键
        skillIndex[(int)req_skills[i][0]] = i;
    }
    
    // 将每个人掌握的技能转换为位掩码表示
    // peopleSkills[i] 表示第i个人掌握的技能集合，用二进制位表示
    int peopleSkills[MAXN];
    for (int i = 0; i < n; i++) {
        peopleSkills[i] = 0;
        // 遍历第i个人掌握的所有技能
        for (int j = 0; j < peopleSkillsCount[i]; j++) {
            // 简单的哈希映射，获取技能索引
            int skillIdx = skillIndex[(int)people[i][j][0]];
            // 将第skillIdx位设为1，表示掌握该技能
            peopleSkills[i] |= 1 << skillIdx;
        }
    }
    
    // dp[mask] 表示覆盖技能集合mask所需的最小团队大小
    // parent[mask] 用于回溯路径，记录选择的人员索引
    // prevState[mask] 记录选择人员之前的技能状态
    int dp[MAX_STATES];
    int parent[MAX_STATES];
    int prevState[MAX_STATES];
    
    // 初始化DP数组
    for (int i = 0; i < (1 << m); i++) {
        dp[i] = INF;
    }
    dp[0] = 0;  // 初始状态：不需要任何技能时，团队大小为0
    
    // 遍历所有可能的技能组合状态
    for (int mask = 0; mask < (1 << m); mask++) {
        // 如果当前状态不可达，跳过
        if (dp[mask] == INF) {
            continue;
        }
        
        // 尝试添加每个人员
        for (int i = 0; i < n; i++) {
            // 添加人员i后的新技能集合
            // mask | peopleSkills[i] 表示将人员i的技能加入当前技能集合
            int newMask = mask | peopleSkills[i];
            
            // 如果通过当前路径能得到更小的团队
            if (dp[newMask] > dp[mask] + 1) {
                dp[newMask] = dp[mask] + 1;
                parent[newMask] = i;      // 记录选择的人员索引
                prevState[newMask] = mask; // 记录选择人员之前的技能状态
            }
        }
    }
    
    // 回溯构建结果团队
    int team[MAXN];
    int teamSize = 0;
    // 从包含所有需求技能的状态开始回溯
    int mask = (1 << m) - 1;
    while (mask > 0) {
        int person = parent[mask];        // 获取选择的人员索引
        team[teamSize++] = person;        // 将人员加入团队
        mask = prevState[mask];           // 回到选择人员之前的技能状态
    }
    
    // 复制结果到输出参数
    for (int i = 0; i < teamSize; i++) {
        result[i] = team[i];
    }
    *resultSize = teamSize;
}

// 主函数 - 用于测试
int main() {
    // 由于编译环境限制，这里不包含测试代码
    // 实际使用时可以添加适当的测试代码
    return 0;
}