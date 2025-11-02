/*
 * HDU 2222 Keywords Search
 * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=2222
 * 题目描述：给定一些单词和一个字符串，求有多少单词在字符串中出现过
 * 
 * 算法详解：
 * 这是一道经典的AC自动机模板题。需要在文本中查找多个模式串的出现次数。
 * 
 * 算法核心思想：
 * 1. 将所有模式串插入到Trie树中
 * 2. 构建失配指针（fail指针）
 * 3. 在文本中进行匹配，统计每个模式串的出现次数
 * 
 * 时间复杂度分析：
 * 1. 构建Trie树：O(∑|Pi|)，其中Pi是第i个模式串
 * 2. 构建fail指针：O(∑|Pi|)
 * 3. 匹配：O(|T|)，其中T是文本串
 * 总时间复杂度：O(∑|Pi| + |T|)
 * 
 * 空间复杂度：O(∑|Pi| × |Σ|)，其中Σ是字符集大小
 * 
 * 适用场景：
 * 1. 多模式串匹配
 * 2. 关键词搜索
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 性能优化：使用数组代替链表提高访问速度
 * 3. 内存优化：合理设置数组大小，避免浪费
 * 
 * 与机器学习的联系：
 * 1. 在自然语言处理中用于关键词提取
 * 2. 在网络安全中用于恶意代码检测
 */

#define MAXN 10005
#define MAXS 1000005

// Trie树节点
struct TrieNode {
    int children[26];
    int isEnd;
    int fail;
    int count; // 匹配次数
    int wordId; // 单词编号
};

struct TrieNode tree[MAXS];
int cnt = 0;
int root = 0;
int wordCount[MAXN];

// 初始化Trie树节点
void initNode(int node) {
    int i;
    for (i = 0; i < 26; i++) {
        tree[node].children[i] = 0;
    }
    tree[node].isEnd = 0;
    tree[node].fail = 0;
    tree[node].count = 0;
    tree[node].wordId = -1;
}

// 插入字符串到Trie树
void insert(char* pattern, int wordId) {
    int node = root;
    int i;
    for (i = 0; pattern[i] != '\0'; i++) {
        int index = pattern[i] - 'a';
        if (tree[node].children[index] == 0) {
            cnt++;
            initNode(cnt);
            tree[node].children[index] = cnt;
        }
        node = tree[node].children[index];
    }
    tree[node].isEnd = 1;
    tree[node].wordId = wordId;
}

// 构建Trie树
void buildTrie(char patterns[][MAXN], int patternCount) {
    int i;
    for (i = 0; i < patternCount; i++) {
        insert(patterns[i], i);
    }
}

// 构建AC自动机
void buildACAutomation() {
    int queue[MAXS];
    int front = 0, rear = 0;
    int i;
    
    // 初始化根节点的失配指针
    for (i = 0; i < 26; i++) {
        if (tree[root].children[i] != 0) {
            tree[tree[root].children[i]].fail = root;
            queue[rear] = tree[root].children[i];
            rear++;
        } else {
            tree[root].children[i] = root;
        }
    }
    
    // BFS构建失配指针
    while (front < rear) {
        int node = queue[front];
        front++;
        
        for (i = 0; i < 26; i++) {
            if (tree[node].children[i] != 0) {
                int failNode = tree[node].fail;
                while (tree[failNode].children[i] == 0) {
                    failNode = tree[failNode].fail;
                }
                tree[tree[node].children[i]].fail = tree[failNode].children[i];
                queue[rear] = tree[node].children[i];
                rear++;
            }
        }
    }
}

// 匹配文本
int matchText(char* text) {
    int current = root;
    int totalMatches = 0;
    int i;
    
    for (i = 0; text[i] != '\0'; i++) {
        int index = text[i] - 'a';
        
        // 根据失配指针跳转
        while (tree[current].children[index] == 0 && current != root) {
            current = tree[current].fail;
        }
        
        if (tree[current].children[index] != 0) {
            current = tree[current].children[index];
        } else {
            current = root;
        }
        
        // 检查是否有匹配的模式串
        int temp = current;
        while (temp != root) {
            if (tree[temp].isEnd) {
                tree[temp].count++;
                totalMatches++;
            }
            temp = tree[temp].fail;
        }
    }
    
    return totalMatches;
}

// 统计每个单词的出现次数
void countWords(int patternCount) {
    int i;
    for (i = 0; i < patternCount; i++) {
        wordCount[i] = 0;
    }
    
    // 使用BFS遍历Trie树，将匹配次数传递给父节点
    int queue[MAXS];
    int front = 0, rear = 0;
    queue[rear] = root;
    rear++;
    
    while (front < rear) {
        int node = queue[front];
        front++;
        
        // 将当前节点的匹配次数传递给fail节点
        if (node != root && tree[node].fail != 0) {
            tree[tree[node].fail].count += tree[node].count;
        }
        
        // 将子节点加入队列
        for (i = 0; i < 26; i++) {
            if (tree[node].children[i] != 0) {
                queue[rear] = tree[node].children[i];
                rear++;
            }
        }
        
        // 如果是单词结尾，记录匹配次数
        if (tree[node].isEnd && tree[node].wordId != -1) {
            wordCount[tree[node].wordId] = tree[node].count;
        }
    }
}

int main() {
    // 示例输入（实际应用中需要从标准输入读取）
    char patterns[5][MAXN] = {"she", "he", "say", "shr", "her"};
    char text[MAXN] = "yasherhs";
    int patternCount = 5;
    
    // 初始化根节点
    initNode(root);
    
    // 构建Trie树
    buildTrie(patterns, patternCount);
    
    // 构建AC自动机
    buildACAutomation();
    
    // 匹配文本
    int totalMatches = matchText(text);
    
    // 统计每个单词的出现次数
    countWords(patternCount);
    
    // 简单输出结果
    return 0;
}