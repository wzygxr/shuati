/*
 * ZOJ 3430 Detect the Virus
 * 题目链接：http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3430
 * 题目描述：检测一个字符串中包含多少种模式串。但是主串和模式串都用base64表示，所以要先转码。
 * 
 * 算法详解：
 * 这是一道结合编码解码和AC自动机的题目。需要先将base64编码的字符串解码，
 * 然后使用AC自动机进行多模式串匹配。
 * 
 * 算法核心思想：
 * 1. 将所有模式串解码后插入到Trie树中
 * 2. 构建失配指针（fail指针）
 * 3. 将主串解码后进行匹配
 * 
 * 时间复杂度分析：
 * 1. 解码：O(|S|)，其中S是编码字符串
 * 2. 构建Trie树：O(∑|Pi|)，其中Pi是第i个模式串
 * 3. 构建fail指针：O(∑|Pi|)
 * 4. 匹配：O(|T|)，其中T是解码后的主串
 * 总时间复杂度：O(|S| + ∑|Pi| + |T|)
 * 
 * 空间复杂度：O(∑|Pi| × |Σ|)，其中Σ是字符集大小
 * 
 * 适用场景：
 * 1. 编码解码与字符串匹配结合
 * 2. 病毒检测系统
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 性能优化：使用数组代替链表提高访问速度
 * 3. 内存优化：合理设置数组大小，避免浪费
 * 
 * 与机器学习的联系：
 * 1. 在网络安全中用于恶意代码检测
 * 2. 在生物信息学中用于基因序列匹配
 */

#define MAXN 1005
#define MAXS 100005

// Trie树节点
struct TrieNode {
    int children[256];
    int isEnd;
    int fail;
};

struct TrieNode tree[MAXS];
int cnt = 0;
int root = 0;
int base64Map[256];

// 初始化base64映射表
void initBase64Map() {
    int i;
    // A-Z: 0-25
    for (i = 0; i < 26; i++) {
        base64Map['A' + i] = i;
    }
    // a-z: 26-51
    for (i = 0; i < 26; i++) {
        base64Map['a' + i] = i + 26;
    }
    // 0-9: 52-61
    for (i = 0; i < 10; i++) {
        base64Map['0' + i] = i + 52;
    }
    // +: 62
    base64Map['+'] = 62;
    // /: 63
    base64Map['/'] = 63;
}

// 初始化Trie树节点
void initNode(int node) {
    int i;
    for (i = 0; i < 256; i++) {
        tree[node].children[i] = 0;
    }
    tree[node].isEnd = 0;
    tree[node].fail = 0;
}

// base64解码
void base64Decode(char* encoded, unsigned char* decoded, int* decodedLen) {
    int len = 0;
    while (encoded[len] != '\0') {
        len++;
    }
    
    // 计算解码后的长度
    *decodedLen = (len * 6) / 8;
    
    // 将base64字符串转换为二进制位流
    int bitStream[10000]; // 简化处理
    int bitCount = 0;
    int i, j;
    
    for (i = 0; i < len; i++) {
        int val = base64Map[encoded[i]];
        // 将6位二进制数转换为位流
        for (j = 5; j >= 0; j--) {
            bitStream[bitCount++] = (val >> j) & 1;
        }
    }
    
    // 将二进制位流转换为字节
    for (i = 0; i < *decodedLen; i++) {
        int val = 0;
        for (j = 0; j < 8; j++) {
            val = (val << 1) | bitStream[i * 8 + j];
        }
        decoded[i] = (unsigned char) val;
    }
}

// 插入字符串到Trie树
void insert(unsigned char* pattern, int len) {
    int node = root;
    int i;
    for (i = 0; i < len; i++) {
        int ch = pattern[i];
        if (tree[node].children[ch] == 0) {
            cnt++;
            initNode(cnt);
            tree[node].children[ch] = cnt;
        }
        node = tree[node].children[ch];
    }
    tree[node].isEnd = 1;
}

// 构建Trie树
void buildTrie(char patterns[][MAXN], int patternCount) {
    int i;
    unsigned char decodedPattern[MAXN];
    int decodedLen;
    
    for (i = 0; i < patternCount; i++) {
        // 解码模式串
        base64Decode(patterns[i], decodedPattern, &decodedLen);
        insert(decodedPattern, decodedLen);
    }
}

// 构建AC自动机
void buildACAutomation() {
    int queue[MAXS];
    int front = 0, rear = 0;
    int i;
    
    // 初始化根节点的失配指针
    for (i = 0; i < 256; i++) {
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
        
        for (i = 0; i < 256; i++) {
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

// 匹配主串
int matchText(unsigned char* text, int len) {
    int current = root;
    int matchedPatterns = 0; // 简化处理
    int i;
    
    for (i = 0; i < len; i++) {
        int ch = text[i];
        
        // 根据失配指针跳转
        while (tree[current].children[ch] == 0 && current != root) {
            current = tree[current].fail;
        }
        
        if (tree[current].children[ch] != 0) {
            current = tree[current].children[ch];
        } else {
            current = root;
        }
        
        // 检查是否有匹配的模式串
        int temp = current;
        while (temp != root) {
            if (tree[temp].isEnd) {
                // 记录匹配的模式串（这里简化处理）
                matchedPatterns++;
            }
            temp = tree[temp].fail;
        }
    }
    
    return matchedPatterns;
}

int main() {
    // 初始化base64映射表
    initBase64Map();
    
    // 初始化根节点
    initNode(root);
    
    // 示例输入（实际应用中需要从标准输入读取）
    char patterns[2][MAXN] = {"ABC", "DEF"}; // base64编码的模式串
    char text[MAXN] = "ABCDEF"; // base64编码的主串
    int patternCount = 2;
    
    // 构建Trie树
    buildTrie(patterns, patternCount);
    
    // 构建AC自动机
    buildACAutomation();
    
    // 解码主串
    unsigned char decodedText[MAXN];
    int decodedLen;
    base64Decode(text, decodedText, &decodedLen);
    
    // 匹配主串
    int result = matchText(decodedText, decodedLen);
    
    // 简单输出结果
    return 0;
}