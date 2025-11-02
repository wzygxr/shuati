/**
 * AC自动机实现 (Aho-Corasick Automaton) (C++简化版本)
 * 
 * AC自动机是一种多模式字符串匹配算法，能够在一次扫描中同时匹配多个模式串。
 * 
 * 算法原理：
 * 1. 构建Trie树：将所有模式串插入到Trie树中
 * 2. 构建fail指针：为Trie树中的每个节点构建失败指针
 * 3. 匹配过程：在文本串中进行匹配，利用fail指针避免重复匹配
 * 
 * 时间复杂度：
 * - 预处理：O(∑|Pi|)，其中∑|Pi|是所有模式串长度之和
 * - 匹配：O(n + z)，其中n是文本串长度，z是匹配次数
 * 
 * 空间复杂度：O(∑|Pi| × |Σ|)，其中|Σ|是字符集大小
 * 
 * 优势：
 * 1. 支持多模式匹配
 * 2. 匹配效率高
 * 3. 适合处理大量模式串的场景
 * 
 * 劣势：
 * 1. 实现复杂度较高
 * 2. 空间消耗较大
 * 3. 对于少量模式串可能不如KMP算法高效
 * 
 * 应用场景：
 * 1. 关键词过滤
 * 2. 病毒特征码检测
 * 3. 生物信息学中的序列匹配
 * 4. 网络入侵检测
 */

// 定义NULL和最大限制
#define NULL 0
#define MAX_PATTERNS 100
#define MAX_PATTERN_LENGTH 100
#define MAX_TEXT_LENGTH 1000
#define CHARSET_SIZE 26

// 静态内存池
static char memoryPool[MAX_PATTERNS * MAX_PATTERN_LENGTH * 10];
static int memoryOffset = 0;

/**
 * Trie树节点
 */
typedef struct Node {
    struct Node* children[CHARSET_SIZE];  // 子节点数组
    struct Node* fail;                    // 失败指针
    int output[MAX_PATTERNS];             // 输出列表，存储以该节点结尾的模式串索引
    int outputCount;                      // 输出列表大小
    int isEnd;                            // 是否为某个模式串的结尾
} Node;

/**
 * AC自动机
 */
typedef struct {
    Node* root;                           // 根节点
    char patterns[MAX_PATTERNS][MAX_PATTERN_LENGTH]; // 模式串列表
    int patternCount;                     // 模式串数量
} ACAutomaton;

/**
 * 匹配结果结构
 */
typedef struct {
    int patternIndex;                     // 模式串索引
    char pattern[MAX_PATTERN_LENGTH];     // 模式串
    int position;                         // 在文本中的位置
} MatchResult;

/**
 * 简单的内存分配函数
 */
void* myMalloc(int size) {
    if (memoryOffset + size > sizeof(memoryPool)) {
        return NULL;
    }
    void* ptr = &memoryPool[memoryOffset];
    memoryOffset += size;
    return ptr;
}

/**
 * 创建新节点
 */
Node* createNode() {
    Node* node = (Node*)myMalloc(sizeof(Node));
    if (node == NULL) return NULL;
    
    for (int i = 0; i < CHARSET_SIZE; i++) {
        node->children[i] = NULL;
    }
    node->fail = NULL;
    node->outputCount = 0;
    node->isEnd = 0;
    return node;
}

/**
 * 初始化AC自动机
 */
void initACAutomaton(ACAutomaton* ac) {
    ac->root = createNode();
    ac->patternCount = 0;
    memoryOffset = 0; // 重置内存池
}

/**
 * 添加模式串
 */
void addPattern(ACAutomaton* ac, const char* pattern) {
    if (ac->patternCount >= MAX_PATTERNS) return;
    
    // 保存模式串
    int len = 0;
    while (pattern[len] != '\0' && len < MAX_PATTERN_LENGTH - 1) {
        ac->patterns[ac->patternCount][len] = pattern[len];
        len++;
    }
    ac->patterns[ac->patternCount][len] = '\0';
    
    Node* current = ac->root;
    
    // 将模式串插入到Trie树中
    for (int i = 0; i < len; i++) {
        char c = pattern[i];
        int index = c - 'a';
        if (index < 0 || index >= CHARSET_SIZE) continue;
        
        if (current->children[index] == NULL) {
            current->children[index] = createNode();
        }
        current = current->children[index];
    }
    
    // 标记该节点为模式串结尾
    current->isEnd = 1;
    current->output[current->outputCount] = ac->patternCount;
    current->outputCount++;
    
    ac->patternCount++;
}

/**
 * 构建失败指针
 */
void buildFailPointer(ACAutomaton* ac) {
    Node* queue[MAX_PATTERNS * MAX_PATTERN_LENGTH];
    int front = 0, rear = 0;
    
    // 初始化根节点的子节点的失败指针
    for (int i = 0; i < CHARSET_SIZE; i++) {
        if (ac->root->children[i] != NULL) {
            ac->root->children[i]->fail = ac->root;
            queue[rear++] = ac->root->children[i];
        } else {
            ac->root->children[i] = ac->root;
        }
    }
    
    // BFS构建失败指针
    while (front < rear) {
        Node* current = queue[front++];
        
        for (int i = 0; i < CHARSET_SIZE; i++) {
            if (current->children[i] != NULL) {
                Node* child = current->children[i];
                Node* failNode = current->fail;
                
                // 找到失败指针指向的节点
                while (failNode->children[i] == NULL && failNode != ac->root) {
                    failNode = failNode->fail;
                }
                
                child->fail = failNode->children[i];
                
                // 合并输出列表
                if (child->fail->isEnd) {
                    for (int j = 0; j < child->fail->outputCount; j++) {
                        child->output[child->outputCount] = child->fail->output[j];
                        child->outputCount++;
                    }
                }
                
                queue[rear++] = child;
            }
        }
    }
}

/**
 * 在文本中查找所有模式串
 */
int search(ACAutomaton* ac, const char* text, MatchResult results[MAX_TEXT_LENGTH]) {
    int resultCount = 0;
    Node* current = ac->root;
    
    int textLen = 0;
    while (text[textLen] != '\0' && textLen < MAX_TEXT_LENGTH) {
        textLen++;
    }
    
    for (int i = 0; i < textLen; i++) {
        char c = text[i];
        int index = c - 'a';
        if (index < 0 || index >= CHARSET_SIZE) continue;
        
        // 如果当前节点没有对应的子节点，则沿着失败指针查找
        while (current->children[index] == NULL && current != ac->root) {
            current = current->fail;
        }
        
        // 移动到下一个节点
        if (current->children[index] != NULL) {
            current = current->children[index];
        }
        
        // 检查是否有模式串匹配
        if (current->isEnd || current->outputCount > 0) {
            for (int j = 0; j < current->outputCount; j++) {
                int patternIndex = current->output[j];
                int patternLen = 0;
                while (ac->patterns[patternIndex][patternLen] != '\0') {
                    patternLen++;
                }
                int position = i - patternLen + 1;
                
                if (resultCount < MAX_TEXT_LENGTH) {
                    results[resultCount].patternIndex = patternIndex;
                    for (int k = 0; k < patternLen && k < MAX_PATTERN_LENGTH - 1; k++) {
                        results[resultCount].pattern[k] = ac->patterns[patternIndex][k];
                    }
                    results[resultCount].pattern[patternLen] = '\0';
                    results[resultCount].position = position;
                    resultCount++;
                }
            }
        }
    }
    
    return resultCount;
}

/**
 * 在文本中查找所有模式串（优化版本）
 */
int searchOptimized(ACAutomaton* ac, const char* text, MatchResult results[MAX_TEXT_LENGTH]) {
    int resultCount = 0;
    Node* current = ac->root;
    
    int textLen = 0;
    while (text[textLen] != '\0' && textLen < MAX_TEXT_LENGTH) {
        textLen++;
    }
    
    for (int i = 0; i < textLen; i++) {
        char c = text[i];
        int index = c - 'a';
        if (index < 0 || index >= CHARSET_SIZE) continue;
        
        // 沿着失败指针查找直到找到匹配的子节点或回到根节点
        while (current->children[index] == NULL && current != ac->root) {
            current = current->fail;
        }
        
        // 移动到下一个节点
        if (current->children[index] != NULL) {
            current = current->children[index];
        }
        
        // 沿着失败指针收集所有匹配结果
        Node* temp = current;
        while (temp != ac->root) {
            if (temp->isEnd) {
                for (int j = 0; j < temp->outputCount; j++) {
                    int patternIndex = temp->output[j];
                    int patternLen = 0;
                    while (ac->patterns[patternIndex][patternLen] != '\0') {
                        patternLen++;
                    }
                    int position = i - patternLen + 1;
                    
                    if (resultCount < MAX_TEXT_LENGTH) {
                        results[resultCount].patternIndex = patternIndex;
                        for (int k = 0; k < patternLen && k < MAX_PATTERN_LENGTH - 1; k++) {
                            results[resultCount].pattern[k] = ac->patterns[patternIndex][k];
                        }
                        results[resultCount].pattern[patternLen] = '\0';
                        results[resultCount].position = position;
                        resultCount++;
                    }
                }
            }
            temp = temp->fail;
        }
    }
    
    return resultCount;
}

/**
 * 获取模式串数量
 */
int getPatternCount(ACAutomaton* ac) {
    return ac->patternCount;
}

// 由于环境限制，不包含main函数和输出语句
// 算法核心功能已实现，可被其他程序调用