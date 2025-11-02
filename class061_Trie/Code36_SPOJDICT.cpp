/**
 * SPOJ DICT - Search in the dictionary!
 * 
 * 题目描述：
 * 给定一个字典（字符串列表）和多个查询，每个查询给出一个前缀，要求找出字典中所有以该前缀开头的单词，
 * 并按字典序输出。
 * 
 * 解题思路：
 * 这是一个标准的Trie树应用问题：
 * 1. 使用Trie树存储字典中的所有单词
 * 2. 对于每个查询，在Trie树中查找前缀对应的节点
 * 3. 从该节点开始深度优先搜索，收集所有单词并按字典序排序输出
 * 
 * 时间复杂度：
 * - 构建Trie树：O(∑len(strings[i]))
 * - 查询：O(len(prefix) + ∑len(results))
 * 空间复杂度：O(∑len(strings[i]))
 */

#include <stdio.h>
#include <stdlib.h>

// 由于环境中缺少标准库头文件，我们使用简化的实现
#define MAX_NODES 1000000
#define MAX_CHILDREN 26
#define MAX_WORD_LENGTH 101
#define MAX_WORDS 10000

/**
 * Trie树节点结构
 */
struct TrieNode {
    int children[MAX_CHILDREN];  // 子节点索引数组，对应a-z
    int isEnd;                   // 标记是否为单词结尾
    int wordIndex;               // 如果是单词结尾，存储单词在数组中的索引
    int valid;                   // 节点是否有效
};

// 全局节点数组和单词数组
struct TrieNode nodes[MAX_NODES];
char words[MAX_WORDS][MAX_WORD_LENGTH];
int node_count;
int word_count;

// 初始化节点
void init_node(int idx) {
    for (int i = 0; i < MAX_CHILDREN; i++) {
        nodes[idx].children[i] = -1;
    }
    nodes[idx].isEnd = 0;
    nodes[idx].wordIndex = -1;
    nodes[idx].valid = 1;
}

// 创建新节点
int create_node() {
    if (node_count >= MAX_NODES) return -1;
    init_node(node_count);
    return node_count++;
}

// 向Trie树中插入一个单词
void insert(char* word) {
    if (word[0] == '\0') {
        return;
    }
    
    // 将单词存储到单词数组中
    int wordIdx = word_count++;
    int i = 0;
    while (word[i] != '\0' && i < MAX_WORD_LENGTH - 1) {
        words[wordIdx][i] = word[i];
        i++;
    }
    words[wordIdx][i] = '\0';
    
    int cur = 0;  // 从根节点开始
    i = 0;
    
    while (word[i] != '\0') {
        int idx = word[i] - 'a';
        if (idx < 0 || idx >= MAX_CHILDREN) return;  // 非法字符
        
        if (nodes[cur].children[idx] == -1) {
            int new_node = create_node();
            if (new_node == -1) return;  // 节点数已达上限
            nodes[cur].children[idx] = new_node;
        }
        
        cur = nodes[cur].children[idx];
        i++;
    }
    
    // 标记单词结尾并存储单词索引
    nodes[cur].isEnd = 1;
    nodes[cur].wordIndex = wordIdx;
}

// 比较两个字符串
int strcmp_custom(char* s1, char* s2) {
    int i = 0;
    while (s1[i] != '\0' && s2[i] != '\0') {
        if (s1[i] < s2[i]) return -1;
        if (s1[i] > s2[i]) return 1;
        i++;
    }
    if (s1[i] == '\0' && s2[i] == '\0') return 0;
    if (s1[i] == '\0') return -1;
    return 1;
}

// 交换两个字符串
void swap_strings(char* s1, char* s2) {
    char temp[MAX_WORD_LENGTH];
    int i = 0;
    while (s1[i] != '\0' && i < MAX_WORD_LENGTH - 1) {
        temp[i] = s1[i];
        s1[i] = s2[i];
        s2[i] = temp[i];
        i++;
    }
    temp[i] = '\0';
    s1[i] = s2[i];
    s2[i] = temp[i];
}

// 简单排序函数
void sort_strings(char result[][MAX_WORD_LENGTH], int count) {
    for (int i = 0; i < count - 1; i++) {
        for (int j = i + 1; j < count; j++) {
            if (strcmp_custom(result[i], result[j]) > 0) {
                swap_strings(result[i], result[j]);
            }
        }
    }
}

// 查找前缀对应的节点
int find_prefix_node(char* prefix) {
    int cur = 0;  // 从根节点开始
    int i = 0;
    
    while (prefix[i] != '\0') {
        int idx = prefix[i] - 'a';
        if (idx < 0 || idx >= MAX_CHILDREN) return -1;  // 非法字符
        
        if (nodes[cur].children[idx] == -1) {
            return -1;  // 前缀不存在
        }
        
        cur = nodes[cur].children[idx];
        i++;
    }
    
    return cur;
}

// 深度优先搜索收集所有单词
int dfs_collect_words(int nodeIdx, char result[][MAX_WORD_LENGTH], int* count) {
    if (*count >= MAX_WORDS) return 0;
    
    if (nodes[nodeIdx].isEnd) {
        int wordIdx = nodes[nodeIdx].wordIndex;
        int i = 0;
        while (words[wordIdx][i] != '\0' && i < MAX_WORD_LENGTH - 1) {
            result[*count][i] = words[wordIdx][i];
            i++;
        }
        result[*count][i] = '\0';
        (*count)++;
    }
    
    // 遍历子节点
    for (int i = 0; i < MAX_CHILDREN; i++) {
        if (nodes[nodeIdx].children[i] != -1) {
            if (!dfs_collect_words(nodes[nodeIdx].children[i], result, count)) {
                return 0;
            }
        }
    }
    
    return 1;
}

// 查找所有以指定前缀开头的单词
int find_words_with_prefix(char* prefix, char result[][MAX_WORD_LENGTH], int* count) {
    *count = 0;
    
    if (prefix[0] == '\0') {
        return 1;
    }
    
    // 查找前缀对应的节点
    int nodeIdx = find_prefix_node(prefix);
    if (nodeIdx == -1) {
        return 1;  // 前缀不存在，返回空结果
    }
    
    // 从该节点开始深度优先搜索，收集所有单词
    dfs_collect_words(nodeIdx, result, count);
    
    // 按字典序排序
    sort_strings(result, *count);
    
    return 1;
}

// 主函数
int main() {
    int caseNum = 1;
    int n;
    
    // 初始化Trie树
    node_count = 0;
    word_count = 0;
    create_node();  // 创建根节点
    
    while (scanf("%d", &n) != EOF) {
        // 插入所有单词
        for (int i = 0; i < n; i++) {
            char word[MAX_WORD_LENGTH];
            scanf("%s", word);
            insert(word);
        }
        
        int m;
        scanf("%d", &m);
        
        // 处理所有查询
        for (int i = 0; i < m; i++) {
            char prefix[MAX_WORD_LENGTH];
            scanf("%s", prefix);
            
            char result[MAX_WORDS][MAX_WORD_LENGTH];
            int count;
            find_words_with_prefix(prefix, result, &count);
            
            printf("Case #%d:\n", caseNum);
            if (count == 0) {
                printf("No match.\n");
            } else {
                for (int j = 0; j < count; j++) {
                    printf("%s\n", result[j]);
                }
            }
            
            caseNum++;
        }
    }
    
    return 0;
}