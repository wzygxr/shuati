/**
 * SPOJ ADAINDEX - Ada and Indexing
 * 
 * 题目描述：
 * 给定一个字符串列表和多个查询，每个查询给出一个前缀，要求统计以该前缀开头的字符串数量。
 * 
 * 解题思路：
 * 这是一个标准的Trie树应用问题。我们可以：
 * 1. 使用Trie树存储所有字符串，在每个节点记录经过该节点的字符串数量
 * 2. 对于每个查询，在Trie树中查找前缀对应的节点
 * 3. 返回该节点记录的字符串数量
 * 
 * 时间复杂度：
 * - 构建Trie树：O(∑len(strings[i]))
 * - 查询：O(len(prefix))
 * 空间复杂度：O(∑len(strings[i]))
 */

#include <stdio.h>
#include <stdlib.h>

// 简化版本，不使用标准库中的map和string
#define MAX_NODES 1000000
#define MAX_CHILDREN 26

/**
 * Trie树节点结构
 */
struct TrieNode {
    int children[MAX_CHILDREN];  // 子节点索引数组，对应a-z
    int count;                   // 经过该节点的字符串数量
    int valid;                   // 节点是否有效
};

// 全局节点数组
TrieNode nodes[MAX_NODES];
int node_count;

// 初始化节点
void init_node(int idx) {
    for (int i = 0; i < MAX_CHILDREN; i++) {
        nodes[idx].children[i] = -1;
    }
    nodes[idx].count = 0;
    nodes[idx].valid = 1;
}

// 创建新节点
int create_node() {
    if (node_count >= MAX_NODES) return -1;
    init_node(node_count);
    return node_count++;
}

// 插入字符串到Trie树
void insert(const char* str) {
    int cur = 0;  // 从根节点开始
    int i = 0;
    
    while (str[i] != '\0') {
        int idx = str[i] - 'a';
        if (idx < 0 || idx >= MAX_CHILDREN) return;  // 非法字符
        
        if (nodes[cur].children[idx] == -1) {
            int new_node = create_node();
            if (new_node == -1) return;  // 节点数已达上限
            nodes[cur].children[idx] = new_node;
        }
        
        cur = nodes[cur].children[idx];
        nodes[cur].count++;  // 增加经过该节点的字符串数量
        i++;
    }
}

// 统计以指定前缀开头的字符串数量
int count_prefix(const char* prefix) {
    int cur = 0;  // 从根节点开始
    int i = 0;
    
    while (prefix[i] != '\0') {
        int idx = prefix[i] - 'a';
        if (idx < 0 || idx >= MAX_CHILDREN) return 0;  // 非法字符
        
        if (nodes[cur].children[idx] == -1) {
            return 0;  // 前缀不存在
        }
        
        cur = nodes[cur].children[idx];
        i++;
    }
    
    // 返回经过该节点的字符串数量
    return nodes[cur].count;
}

// 主函数
int main() {
    int n, m;
    
    // 初始化Trie树
    node_count = 0;
    create_node();  // 创建根节点
    
    // 读取n和m
    scanf("%d %d", &n, &m);
    
    // 插入所有字符串
    for (int i = 0; i < n; i++) {
        char word[1000];
        scanf("%s", word);
        insert(word);
    }
    
    // 处理所有查询
    for (int i = 0; i < m; i++) {
        char prefix[1000];
        scanf("%s", prefix);
        int count = count_prefix(prefix);
        printf("%d\n", count);
    }
    
    return 0;
}