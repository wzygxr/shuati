// 单词接龙 II
// 按字典 wordList 完成从单词 beginWord 到单词 endWord 转化
// 一个表示此过程的 转换序列 是形式上像 
// beginWord -> s1 -> s2 -> ... -> sk 这样的单词序列，并满足：
// 每对相邻的单词之间仅有单个字母不同
// 转换过程中的每个单词 si（1 <= i <= k）必须是字典 wordList 中的单词
// 注意，beginWord 不必是字典 wordList 中的单词
// sk == endWord
// 给你两个单词 beginWord 和 endWord ，以及一个字典 wordList
// 请你找出并返回所有从 beginWord 到 endWord 的 最短转换序列
// 如果不存在这样的转换序列，返回一个空列表
// 每个序列都应该以单词列表 [beginWord, s1, s2, ..., sk] 的形式返回
// 测试链接 : https://leetcode.cn/problems/word-ladder-ii/
// 
// 算法思路：
// 使用双向BFS构建图，然后使用DFS找到所有最短路径
// 1. 使用BFS从beginWord开始构建反向图（从endWord指向beginWord）
// 2. 在BFS过程中，只扩展能到达endWord的节点
// 3. 使用DFS在构建的图中找到所有从endWord到beginWord的路径
// 4. 将路径反转得到从beginWord到endWord的路径
// 
// 时间复杂度：O(N * M^2 + M * N^2)，其中N是单词数量，M是单词长度
// 空间复杂度：O(N * M^2)，用于存储图和访问状态
// 
// 工程化考量：
// 1. 使用数组存储图结构
// 2. 使用简单的字符串比较
// 3. 使用双向BFS优化搜索效率

#define MAXN 1000
#define MAXM 100

// 简单的字符串比较函数
int str_equal(char* a, char* b) {
    int i = 0;
    while (a[i] != '\0' && b[i] != '\0') {
        if (a[i] != b[i]) {
            return 0;
        }
        i++;
    }
    return a[i] == '\0' && b[i] == '\0';
}

// 计算两个字符串之间的差异字符数
int diff_count(char* a, char* b) {
    int count = 0;
    int i = 0;
    while (a[i] != '\0' && b[i] != '\0') {
        if (a[i] != b[i]) {
            count++;
        }
        i++;
    }
    // 如果长度不同，也认为不匹配
    if (a[i] != '\0' || b[i] != '\0') {
        return MAXM;
    }
    return count;
}

// 图结构
int graph[MAXN][MAXN];  // 邻接表
int graph_size[MAXN];   // 每个节点的邻接点数量

// 当前层和下一层
int cur_level[MAXN];
int cur_level_size;
int next_level[MAXN];
int next_level_size;

// 路径和结果
int path[MAXN];
int path_size;
int result[MAXN][MAXN];
int result_size[MAXN];
int results_count;

// 清空图
void clear_graph() {
    int i, j;
    for (i = 0; i < MAXN; i++) {
        graph_size[i] = 0;
        for (j = 0; j < MAXN; j++) {
            graph[i][j] = 0;
        }
    }
}

// 查找单词在列表中的索引
int find_word(char* word, char** wordList, int wordListSize) {
    int i;
    for (i = 0; i < wordListSize; i++) {
        if (str_equal(word, wordList[i])) {
            return i;
        }
    }
    return -1;
}

// 添加边到图中
void add_edge(int from, int to) {
    graph[from][graph_size[from]] = to;
    graph_size[from]++;
}

// DFS查找所有路径
void dfs(int word_idx, int begin_idx, int end_idx, char** wordList, int wordListSize) {
    path[path_size] = word_idx;
    path_size++;
    
    // 如果到达起始单词
    if (word_idx == begin_idx) {
        // 将路径反转后添加到结果中
        result_size[results_count] = path_size;
        int i;
        for (i = 0; i < path_size; i++) {
            result[results_count][i] = path[path_size - 1 - i];
        }
        results_count++;
    } else {
        // 递归处理所有前驱单词
        int i;
        for (i = 0; i < graph_size[word_idx]; i++) {
            int next_word_idx = graph[word_idx][i];
            dfs(next_word_idx, begin_idx, end_idx, wordList, wordListSize);
        }
    }
    
    path_size--;
}

// 找出所有从beginWord到endWord的最短转换序列
int findLadders(char* beginWord, char* endWord, char** wordList, int wordListSize) {
    // 初始化
    clear_graph();
    results_count = 0;
    
    // 查找起始和结束单词的索引
    int begin_idx = -1;
    int end_idx = find_word(endWord, wordList, wordListSize);
    
    // 如果目标单词不在词典中，直接返回0
    if (end_idx == -1) {
        return 0;
    }
    
    // 查找起始单词是否在词典中
    begin_idx = find_word(beginWord, wordList, wordListSize);
    if (begin_idx == -1) {
        // 如果不在词典中，添加到词典末尾
        begin_idx = wordListSize;
    }
    
    // 使用BFS构建图
    cur_level_size = 1;
    cur_level[0] = (begin_idx == wordListSize) ? -1 : begin_idx; // -1表示beginWord不在词典中
    int found = 0;
    
    // 标记已访问的单词
    int visited[MAXN];
    int i, j;
    for (i = 0; i < wordListSize; i++) {
        visited[i] = 0;
    }
    
    // BFS搜索
    while (cur_level_size > 0 && !found) {
        // 标记当前层的单词为已访问
        for (i = 0; i < cur_level_size; i++) {
            if (cur_level[i] != -1) {
                visited[cur_level[i]] = 1;
            }
        }
        
        // 处理当前层的所有单词
        next_level_size = 0;
        for (i = 0; i < cur_level_size; i++) {
            int word_idx = cur_level[i];
            
            // 遍历词典中的所有单词
            for (j = 0; j < wordListSize; j++) {
                // 如果单词未被访问
                if (!visited[j]) {
                    char* word = (word_idx == -1) ? beginWord : wordList[word_idx];
                    // 如果两个单词只有一个字符不同
                    if (diff_count(word, wordList[j]) == 1) {
                        // 如果找到了目标单词
                        if (str_equal(wordList[j], endWord)) {
                            found = 1;
                        }
                        // 在反向图中添加边
                        add_edge(j, word_idx);
                        // 将新单词加入下一层
                        next_level[next_level_size] = j;
                        next_level_size++;
                    }
                }
            }
        }
        
        // 更新当前层
        for (i = 0; i < next_level_size; i++) {
            cur_level[i] = next_level[i];
        }
        cur_level_size = next_level_size;
    }
    
    // 如果找到了目标单词，使用DFS搜索所有路径
    if (found) {
        path_size = 0;
        dfs(end_idx, (begin_idx == wordListSize) ? -1 : begin_idx, end_idx, wordList, wordListSize);
        return results_count;
    } else {
        return 0;
    }
}