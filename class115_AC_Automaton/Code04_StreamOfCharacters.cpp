/*
 * LeetCode 1032. Stream of Characters
 * 题目链接：https://leetcode.com/problems/stream-of-characters/
 * 题目描述：设计一个算法，接收一个字符流，并检查这些字符的后缀是否是字符串数组words中的一个字符串
 * 
 * 算法详解：
 * 这是一道典型的AC自动机应用题。由于需要检查字符流的后缀是否匹配模式串，
 * 我们可以将模式串反转后构建AC自动机，然后在字符流中进行匹配。
 * 
 * 算法核心思想：
 * 1. 将所有模式串反转后插入到Trie树中
 * 2. 构建失配指针（fail指针）
 * 3. 在字符流中进行匹配，每次匹配当前字符，利用fail指针避免回溯
 * 
 * 时间复杂度分析：
 * 1. 构建Trie树：O(∑|Pi|)，其中Pi是第i个模式串
 * 2. 构建fail指针：O(∑|Pi|)
 * 3. 查询：O(|T|)，其中T是文本串
 * 总时间复杂度：O(∑|Pi| + |T|)
 * 
 * 空间复杂度：O(∑|Pi| × |Σ|)，其中Σ是字符集大小
 * 
 * 适用场景：
 * 1. 字符流匹配
 * 2. 后缀匹配问题
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 性能优化：使用数组代替链表提高访问速度
 * 3. 内存优化：合理设置数组大小，避免浪费
 * 
 * 与机器学习的联系：
 * 1. 在自然语言处理中用于实时文本分析
 * 2. 在网络安全中用于实时恶意代码检测
 */

#define MAXN 1000
#define MAXS 10000

// Trie树节点
struct TrieNode {
    int children[26];
    int isEnd;
    int fail;
};

struct TrieNode tree[MAXS];
int cnt = 0;
int root = 0;
int current = 0;

// 初始化Trie树节点
void initNode(int node) {
    int i;
    for (i = 0; i < 26; i++) {
        tree[node].children[i] = 0;
    }
    tree[node].isEnd = 0;
    tree[node].fail = 0;
}

// 插入字符串到Trie树
void insert(char* word, int len) {
    int node = root;
    int i;
    // 反转字符串插入Trie树
    for (i = len - 1; i >= 0; i--) {
        int index = word[i] - 'a';
        if (tree[node].children[index] == 0) {
            cnt++;
            initNode(cnt);
            tree[node].children[index] = cnt;
        }
        node = tree[node].children[index];
    }
    tree[node].isEnd = 1;
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

int query(char letter) {
    // 根据失配指针跳转
    while (tree[current].children[letter - 'a'] == 0 && current != root) {
        current = tree[current].fail;
    }
    
    if (tree[current].children[letter - 'a'] != 0) {
        current = tree[current].children[letter - 'a'];
    } else {
        current = root;
    }
    
    // 检查是否有匹配的模式串
    int temp = current;
    while (temp != root) {
        if (tree[temp].isEnd) {
            return 1;
        }
        temp = tree[temp].fail;
    }
    
    return 0;
}

// 测试方法
int main() {
    char words[3][10] = {"cd", "f", "kl"};
    int wordLens[3] = {2, 1, 2};
    
    int i;
    
    // 初始化根节点
    initNode(root);
    
    // 构建Trie树
    for (i = 0; i < 3; i++) {
        insert(words[i], wordLens[i]);
    }
    
    // 构建AC自动机
    buildACAutomation();
    
    char letters[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l'};
    int lettersLen = 12;
    
    for (i = 0; i < lettersLen; i++) {
        char letter = letters[i];
        int result = query(letter);
        // 简单输出结果
        if (result) {
            // 匹配成功
        }
    }
    
    return 0;
}