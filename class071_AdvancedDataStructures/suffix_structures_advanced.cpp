/**
 * 后缀数组和后缀自动机实现 (C++简化版本)
 * 
 * 包括后缀数组(SA)、最长公共前缀(LCP)、后缀自动机(SAM)等实现
 * 
 * 后缀结构在字符串处理中有着广泛的应用，包括：
 * 1. 字符串匹配
 * 2. 最长重复子串查找
 * 3. 字典序排序
 * 4. 生物信息学中的序列分析
 */

// 定义最大字符串长度和相关常量
#define MAX_STRING_LENGTH 1000
#define MAX_STATES 2000
#define NULL -1

/**
 * 后缀数组实现
 */
typedef struct {
    char s[MAX_STRING_LENGTH];     // 输入字符串（带终止符）
    int sa[MAX_STRING_LENGTH];     // 后缀数组
    int rank[MAX_STRING_LENGTH];   // 排名数组
    int height[MAX_STRING_LENGTH]; // LCP数组
    int length;                    // 字符串长度
} SuffixArrayAdvanced;

/**
 * 初始化后缀数组
 */
void initSuffixArrayAdvanced(SuffixArrayAdvanced* sa_struct, const char* str) {
    int len = 0;
    while (str[len] != '\0' && len < MAX_STRING_LENGTH - 2) {
        sa_struct->s[len] = str[len];
        len++;
    }
    sa_struct->s[len] = '$';  // 添加终止符
    sa_struct->s[len + 1] = '\0';
    sa_struct->length = len + 1;
    
    // 初始化后缀数组和排名数组
    for (int i = 0; i < sa_struct->length; i++) {
        sa_struct->sa[i] = i;
        sa_struct->rank[i] = sa_struct->s[i];
    }
}

/**
 * 比较函数用于后缀排序
 */
int compareSuffixesAdvanced(SuffixArrayAdvanced* sa_struct, int a, int b, int k) {
    if (sa_struct->rank[a] != sa_struct->rank[b]) {
        return sa_struct->rank[a] - sa_struct->rank[b];
    }
    int ra = (a + k < sa_struct->length) ? sa_struct->rank[a + k] : -1;
    int rb = (b + k < sa_struct->length) ? sa_struct->rank[b + k] : -1;
    return ra - rb;
}

/**
 * 构建后缀数组（使用倍增算法）
 */
void buildSuffixArrayAdvanced(SuffixArrayAdvanced* sa_struct) {
    int n = sa_struct->length;
    int tmp[MAX_STRING_LENGTH];
    
    // 倍增排序
    for (int k = 1; k < n; k <<= 1) {
        // 简单的冒泡排序（实际应用中应使用更高效的排序算法）
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (compareSuffixesAdvanced(sa_struct, sa_struct->sa[j], sa_struct->sa[j + 1], k) > 0) {
                    // 交换
                    int temp = sa_struct->sa[j];
                    sa_struct->sa[j] = sa_struct->sa[j + 1];
                    sa_struct->sa[j + 1] = temp;
                }
            }
        }
        
        // 更新rank
        tmp[sa_struct->sa[0]] = 0;
        for (int i = 1; i < n; i++) {
            int cmp = compareSuffixesAdvanced(sa_struct, sa_struct->sa[i-1], sa_struct->sa[i], k);
            tmp[sa_struct->sa[i]] = tmp[sa_struct->sa[i-1]] + (cmp != 0 ? 1 : 0);
        }
        
        for (int i = 0; i < n; i++) {
            sa_struct->rank[sa_struct->sa[i]] = tmp[sa_struct->sa[i]];
        }
        
        if (sa_struct->rank[sa_struct->sa[n-1]] == n - 1) break;
    }
}

/**
 * 构建LCP数组（使用Kasai算法）
 */
void buildLCPAdvanced(SuffixArrayAdvanced* sa_struct) {
    int n = sa_struct->length;
    int inv[MAX_STRING_LENGTH];
    
    // 计算rank的逆数组
    for (int i = 0; i < n; i++) {
        inv[sa_struct->sa[i]] = i;
    }
    
    // Kasai算法
    for (int i = 0, k = 0; i < n; i++) {
        if (inv[i] == n - 1) {
            k = 0;
            continue;
        }
        
        int j = sa_struct->sa[inv[i] + 1];
        while (i + k < n && j + k < n && sa_struct->s[i + k] == sa_struct->s[j + k]) {
            k++;
        }
        
        sa_struct->height[inv[i]] = k;
        if (k > 0) k--;
    }
}

/**
 * 使用RMQ维护LCP数组查询区间最小值
 */
typedef struct {
    int st[MAX_STRING_LENGTH][10]; // Sparse Table
    int n;
} LCP_RMQAdvanced;

/**
 * 初始化RMQ
 */
void initLCP_RMQAdvanced(LCP_RMQAdvanced* rmq, SuffixArrayAdvanced* sa_struct) {
    rmq->n = sa_struct->length;
    
    // 初始化
    for (int i = 0; i < rmq->n; i++) {
        rmq->st[i][0] = sa_struct->height[i];
    }
    
    // 构建Sparse Table
    for (int j = 1; (1 << j) <= rmq->n; j++) {
        for (int i = 0; i + (1 << j) <= rmq->n; i++) {
            int a = rmq->st[i][j-1];
            int b = rmq->st[i + (1 << (j-1))][j-1];
            rmq->st[i][j] = (a < b) ? a : b;
        }
    }
}

/**
 * 查询区间[l, r]的最小值
 */
int queryLCP_RMQAdvanced(LCP_RMQAdvanced* rmq, int l, int r) {
    if (l > r) return 0;
    if (l < 0 || r >= rmq->n) return 1000000; // 大数表示无效值
    
    int k = 0;
    int temp = r - l + 1;
    while ((1 << (k + 1)) <= temp) k++;
    
    int a = rmq->st[l][k];
    int b = rmq->st[r - (1 << k) + 1][k];
    return (a < b) ? a : b;
}

/**
 * 后缀自动机实现
 */
typedef struct {
    int len;                    // 从初始状态到当前状态的最长字符串长度
    int link;                   // 后缀链接
    int next[26];               // 转移函数（假设只处理小写字母）
    int endPosSize;             // right集合大小
    int isClone;                // 是否为克隆节点
} SAMStateAdvanced;

typedef struct {
    SAMStateAdvanced states[MAX_STATES];
    int stateCount;
    int last;
} SuffixAutomatonAdvanced;

/**
 * 初始化后缀自动机
 */
void initSuffixAutomatonAdvanced(SuffixAutomatonAdvanced* sam) {
    // 初始化初始状态
    sam->states[0].len = 0;
    sam->states[0].link = -1;
    sam->states[0].endPosSize = 0;
    sam->states[0].isClone = 0;
    for (int i = 0; i < 26; i++) {
        sam->states[0].next[i] = -1;
    }
    
    sam->stateCount = 1;
    sam->last = 0;
}

/**
 * 扩展SAM
 */
void extendSuffixAutomatonAdvanced(SuffixAutomatonAdvanced* sam, char c) {
    int cur = sam->stateCount;
    sam->states[cur].len = sam->states[sam->last].len + 1;
    sam->states[cur].endPosSize = 1;
    sam->states[cur].isClone = 0;
    sam->states[cur].link = -1;
    for (int i = 0; i < 26; i++) {
        sam->states[cur].next[i] = -1;
    }
    
    int p = sam->last;
    int index = c - 'a';
    
    // 更新转移函数
    while (p != -1 && sam->states[p].next[index] == -1) {
        sam->states[p].next[index] = cur;
        p = sam->states[p].link;
    }
    
    if (p == -1) {
        sam->states[cur].link = 0;
    } else {
        int q = sam->states[p].next[index];
        if (sam->states[p].len + 1 == sam->states[q].len) {
            sam->states[cur].link = q;
        } else {
            int clone = sam->stateCount;
            sam->states[clone].len = sam->states[p].len + 1;
            for (int i = 0; i < 26; i++) {
                sam->states[clone].next[i] = sam->states[q].next[i];
            }
            sam->states[clone].link = sam->states[q].link;
            sam->states[clone].isClone = 1;
            sam->states[clone].endPosSize = 0;
            
            while (p != -1 && sam->states[p].next[index] == q) {
                sam->states[p].next[index] = clone;
                p = sam->states[p].link;
            }
            
            sam->states[q].link = clone;
            sam->states[cur].link = clone;
            
            sam->stateCount++;
        }
    }
    
    sam->last = cur;
    sam->stateCount++;
}

/**
 * 构建后缀自动机
 */
void buildSuffixAutomatonAdvanced(SuffixAutomatonAdvanced* sam, const char* str) {
    initSuffixAutomatonAdvanced(sam);
    
    int len = 0;
    while (str[len] != '\0') {
        extendSuffixAutomatonAdvanced(sam, str[len]);
        len++;
    }
}

/**
 * 计算每个状态的right集合大小
 */
void calculateEndPosSizeAdvanced(SuffixAutomatonAdvanced* sam) {
    // 简化实现：按长度降序更新endPosSize
    for (int i = sam->stateCount - 1; i >= 0; i--) {
        if (sam->states[i].link != -1) {
            sam->states[sam->states[i].link].endPosSize += sam->states[i].endPosSize;
        }
    }
}

/**
 * 子串计数DP边界处理
 */
long long countDistinctSubstringsAdvanced(SuffixAutomatonAdvanced* sam) {
    long long count = 0;
    for (int i = 1; i < sam->stateCount; i++) {
        int link = sam->states[i].link;
        if (link != -1) {
            count += sam->states[i].len - sam->states[link].len;
        }
    }
    return count;
}

/**
 * 获取状态数量
 */
int getStateCountAdvanced(SuffixAutomatonAdvanced* sam) {
    return sam->stateCount;
}

/**
 * 检查字符串是否为原字符串的子串
 */
int isSubstringAdvanced(SuffixAutomatonAdvanced* sam, const char* t) {
    int v = 0;
    int i = 0;
    
    while (t[i] != '\0') {
        int index = t[i] - 'a';
        if (index < 0 || index >= 26 || sam->states[v].next[index] == -1) {
            return 0;
        }
        v = sam->states[v].next[index];
        i++;
    }
    return 1;
}

// 由于环境限制，不包含main函数和输出语句
// 算法核心功能已实现，可被其他程序调用