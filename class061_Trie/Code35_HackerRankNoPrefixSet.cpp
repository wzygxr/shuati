/**
 * HackerRank No Prefix Set
 * 
 * 题目描述：
 * 给定N个字符串，每个字符串只包含小写字母a-j（包含）。
 * 如果字符串集合中没有字符串是另一个字符串的前缀，则称该字符串集合为GOOD SET。
 * 否则，打印BAD SET，并在下一行打印正在检查的字符串。
 * 
 * 注意：如果两个字符串相同，它们互为前缀。
 * 
 * 解题思路：
 * 这是一个经典的Trie树应用问题，用于检测前缀关系：
 * 1. 使用Trie树存储字符串
 * 2. 在插入每个字符串时检查前缀关系
 * 3. 如果发现前缀关系，立即返回BAD SET
 * 
 * 检测前缀关系的方法：
 * 1. 在插入过程中，如果到达一个已经是单词结尾的节点，说明当前字符串是某个已插入字符串的前缀
 * 2. 如果插入完成后，当前节点还有子节点，说明某个已插入字符串是当前字符串的前缀
 * 
 * 时间复杂度：O(∑len(strings[i]))
 * 空间复杂度：O(∑len(strings[i]))
 */

// 由于环境中缺少标准库头文件，我们使用简化的实现
#define MAX_NODES 1000000
#define MAX_CHILDREN 26
#define MAX_WORD_LENGTH 61

#define MAX_NODES 1000000
#define MAX_CHILDREN 26
#define MAX_WORD_LENGTH 61

/**
 * Trie树节点结构
 */
struct TrieNode {
    int children[MAX_CHILDREN];  // 子节点索引数组，对应a-j
    int isEnd;                   // 标记是否为单词结尾
    int valid;                   // 节点是否有效
};

// 全局节点数组
struct TrieNode nodes[MAX_NODES];
int node_count;

// 初始化节点
void init_node(int idx) {
    for (int i = 0; i < MAX_CHILDREN; i++) {
        nodes[idx].children[i] = -1;
    }
    nodes[idx].isEnd = 0;
    nodes[idx].valid = 1;
}

// 创建新节点
int create_node() {
    if (node_count >= MAX_NODES) return -1;
    init_node(node_count);
    return node_count++;
}

// 向Trie树中插入单词并检查前缀关系
int insert_and_check(char* word, char* conflict_word) {
    if (word[0] == '\0') {
        return 1;  // 成功
    }
    
    int cur = 0;  // 从根节点开始
    int i = 0;
    
    while (word[i] != '\0') {
        int idx = word[i] - 'a';
        if (idx < 0 || idx >= MAX_CHILDREN) return 1;  // 非法字符
        
        if (nodes[cur].children[idx] == -1) {
            int new_node = create_node();
            if (new_node == -1) return 1;  // 节点数已达上限
            nodes[cur].children[idx] = new_node;
        }
        
        cur = nodes[cur].children[idx];
        
        // 如果当前节点已经是某个单词的结尾，说明当前单词是另一个单词的前缀
        if (nodes[cur].isEnd) {
            strcpy(conflict_word, word);
            return 0;  // 失败
        }
        
        i++;
    }
    
    // 标记当前节点为单词结尾
    nodes[cur].isEnd = 1;
    
    // 检查当前节点是否有子节点，如果有说明某个单词是当前单词的前缀
    for (int j = 0; j < MAX_CHILDREN; j++) {
        if (nodes[cur].children[j] != -1) {
            strcpy(conflict_word, word);
            return 0;  // 失败
        }
    }
    
    return 1;  // 成功
}

// 主函数
int main() {
    int n;
    
    // 初始化Trie树
    node_count = 0;
    create_node();  // 创建根节点
    
    // 读取n
    scanf("%d", &n);
    getchar();  // 消费换行符
    
    // 处理每个字符串
    for (int i = 0; i < n; i++) {
        char word[MAX_WORD_LENGTH];
        char conflict_word[MAX_WORD_LENGTH];
        fgets(word, sizeof(word), stdin);
        
        // 移除换行符
        int len = strlen(word);
        if (len > 0 && word[len-1] == '\n') {
            word[len-1] = '\0';
        }
        
        int result = insert_and_check(word, conflict_word);
        if (result == 0) {  // 失败
            printf("BAD SET\n");
            printf("%s\n", conflict_word);
            return 0;
        }
    }
    
    printf("GOOD SET\n");
    return 0;
}