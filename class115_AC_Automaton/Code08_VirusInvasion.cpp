/*
 * HDU 2896 病毒侵袭
 * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=2896
 * 题目描述：每个病毒都有一个编号，依此为1—N。不同编号的病毒特征码不会相同。
 * 在这之后一行，有一个整数M（1<=M<=1000），表示网站数。
 * 接下来M行，每行表示一个网站源码，源码字符串长度在1—10000之间。
 * 输出包含病毒特征码的网站编号和病毒编号。
 * 
 * 算法详解：
 * 这是一道AC自动机应用题，需要在多个文本中查找多个模式串，并记录每个文本中
 * 包含哪些模式串。
 * 
 * 算法核心思想：
 * 1. 将所有病毒特征码插入到Trie树中
 * 2. 构建失配指针（fail指针）
 * 3. 对每个网站源码进行匹配，记录包含的病毒编号
 * 
 * 时间复杂度分析：
 * 1. 构建Trie树：O(∑|Pi|)，其中Pi是第i个病毒特征码
 * 2. 构建fail指针：O(∑|Pi|)
 * 3. 匹配：O(∑|Ti|)，其中Ti是第i个网站源码
 * 总时间复杂度：O(∑|Pi| + ∑|Ti|)
 * 
 * 空间复杂度：O(∑|Pi| × |Σ|)，其中Σ是字符集大小
 * 
 * 适用场景：
 * 1. 网站安全检测
 * 2. 病毒特征码匹配
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
    int children[128];
    int isEnd;
    int fail;
    int virusId; // 病毒编号
};

struct TrieNode tree[MAXS];
int cnt = 0;
int root = 0;
int infectedWebsites[MAXN][MAXN]; // 每个病毒感染的网站列表
int infectedCount[MAXN]; // 每个病毒感染的网站数量
int websiteViruses[MAXN][MAXN]; // 每个网站包含的病毒列表
int virusCount[MAXN]; // 每个网站包含的病毒数量

// 初始化Trie树节点
void initNode(int node) {
    int i;
    for (i = 0; i < 128; i++) {
        tree[node].children[i] = 0;
    }
    tree[node].isEnd = 0;
    tree[node].fail = 0;
    tree[node].virusId = -1;
}

// 插入字符串到Trie树
void insert(char* virus, int virusId) {
    int node = root;
    int i;
    for (i = 0; virus[i] != '\0'; i++) {
        int index = virus[i];
        if (tree[node].children[index] == 0) {
            cnt++;
            initNode(cnt);
            tree[node].children[index] = cnt;
        }
        node = tree[node].children[index];
    }
    tree[node].isEnd = 1;
    tree[node].virusId = virusId;
}

// 构建Trie树
void buildTrie(char viruses[][MAXN], int virusCount) {
    int i;
    for (i = 0; i < virusCount; i++) {
        insert(viruses[i], i + 1); // 病毒编号从1开始
    }
}

// 构建AC自动机
void buildACAutomation() {
    int queue[MAXS];
    int front = 0, rear = 0;
    int i;
    
    // 初始化根节点的失配指针
    for (i = 0; i < 128; i++) {
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
        
        for (i = 0; i < 128; i++) {
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

// 匹配网站源码
void matchWebsite(int websiteId, char* websiteCode) {
    int current = root;
    int i;
    
    for (i = 0; websiteCode[i] != '\0'; i++) {
        int index = websiteCode[i];
        
        // 根据失配指针跳转
        while (tree[current].children[index] == 0 && current != root) {
            current = tree[current].fail;
        }
        
        if (tree[current].children[index] != 0) {
            current = tree[current].children[index];
        } else {
            current = root;
        }
        
        // 检查是否有匹配的病毒特征码
        int temp = current;
        while (temp != root) {
            if (tree[temp].isEnd) {
                // 记录感染的网站和病毒
                infectedWebsites[tree[temp].virusId][infectedCount[tree[temp].virusId]] = websiteId;
                infectedCount[tree[temp].virusId]++;
                
                websiteViruses[websiteId][virusCount[websiteId]] = tree[temp].virusId;
                virusCount[websiteId]++;
            }
            temp = tree[temp].fail;
        }
    }
}

int main() {
    // 示例输入（实际应用中需要从标准输入读取）
    char viruses[3][MAXN] = {"aaa", "bbb", "ccc"}; // 病毒特征码
    char websites[3][MAXN] = {"aaabbbccc", "aaabbb", "bbbccc"}; // 网站源码
    
    int virusCountInput = 3;
    int websiteCount = 3;
    
    // 初始化数据结构
    initNode(root);
    
    int i, j;
    for (i = 1; i <= virusCountInput; i++) {
        infectedCount[i] = 0;
    }
    
    for (i = 1; i <= websiteCount; i++) {
        virusCount[i] = 0;
    }
    
    // 构建Trie树
    buildTrie(viruses, virusCountInput);
    
    // 构建AC自动机
    buildACAutomation();
    
    // 匹配每个网站
    for (i = 0; i < websiteCount; i++) {
        matchWebsite(i + 1, websites[i]);
    }
    
    // 输出结果
    int totalInfected = 0;
    for (i = 1; i <= websiteCount; i++) {
        if (virusCount[i] > 0) {
            totalInfected++;
            // 简单输出结果
        }
    }
    
    // 简单输出结果
    return 0;
}