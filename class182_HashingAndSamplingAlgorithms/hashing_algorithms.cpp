/**
 * 哈希算法实现 (C++简化版本)
 * 
 * 包括双模哈希、三模哈希、前缀哈希等实现
 * 
 * 哈希算法在计算机科学中有着广泛的应用，包括：
 * 1. 数据结构：哈希表、布隆过滤器
 * 2. 密码学：数字签名、消息认证
 * 3. 数据完整性：校验和、数字指纹
 * 4. 数据库：索引、分区
 * 5. 网络：负载均衡、缓存
 */

// 定义最大字符串长度
#define MAX_STRING_LENGTH 1000
#define MAX_VERSIONS 100

// 常用的大质数，用于哈希计算
#define MOD1 1000000007LL  // 10^9 + 7
#define MOD2 1000000009LL  // 10^9 + 9
#define MOD3 998244353LL   // 常用的NTT模数
#define BASE 31LL          // 哈希基数

/**
 * 单模哈希
 * 使用单个大质数作为模数
 */
typedef struct {
    long long hash[MAX_STRING_LENGTH + 1];
    long long pow[MAX_STRING_LENGTH + 1];
    long long mod;
    int length;
} SingleHash;

/**
 * 初始化单模哈希
 */
void initSingleHash(SingleHash* sh, const char* s, long long mod) {
    sh->mod = mod;
    sh->length = 0;
    
    // 计算字符串长度
    while (s[sh->length] != '\0' && sh->length < MAX_STRING_LENGTH) {
        sh->length++;
    }
    
    // 预计算幂次
    sh->pow[0] = 1;
    for (int i = 1; i <= sh->length; i++) {
        sh->pow[i] = (sh->pow[i - 1] * BASE) % mod;
    }
    
    // 计算前缀哈希
    sh->hash[0] = 0;
    for (int i = 0; i < sh->length; i++) {
        sh->hash[i + 1] = (sh->hash[i] * BASE + (s[i] - 'a' + 1)) % mod;
    }
}

/**
 * 获取子串的哈希值
 */
long long getSingleHash(SingleHash* sh, int l, int r) {
    long long result = (sh->hash[r + 1] - sh->hash[l] * sh->pow[r - l + 1]) % sh->mod;
    if (result < 0) result += sh->mod;
    return result;
}

/**
 * 双模哈希
 * 使用两个大质数作为模数，降低哈希碰撞概率
 */
typedef struct {
    SingleHash hash1;
    SingleHash hash2;
} DoubleHash;

/**
 * 初始化双模哈希
 */
void initDoubleHash(DoubleHash* dh, const char* s) {
    initSingleHash(&dh->hash1, s, MOD1);
    initSingleHash(&dh->hash2, s, MOD2);
}

/**
 * 获取子串的双模哈希值
 */
void getDoubleHash(DoubleHash* dh, int l, int r, long long result[2]) {
    result[0] = getSingleHash(&dh->hash1, l, r);
    result[1] = getSingleHash(&dh->hash2, l, r);
}

/**
 * 比较两个子串是否相等
 */
int doubleHashEquals(DoubleHash* dh, int l1, int r1, int l2, int r2) {
    long long hash1[2], hash2[2];
    getDoubleHash(dh, l1, r1, hash1);
    getDoubleHash(dh, l2, r2, hash2);
    return (hash1[0] == hash2[0] && hash1[1] == hash2[1]);
}

/**
 * 三模哈希
 * 使用三个大质数作为模数，进一步降低哈希碰撞概率
 */
typedef struct {
    SingleHash hash1;
    SingleHash hash2;
    SingleHash hash3;
} TripleHash;

/**
 * 初始化三模哈希
 */
void initTripleHash(TripleHash* th, const char* s) {
    initSingleHash(&th->hash1, s, MOD1);
    initSingleHash(&th->hash2, s, MOD2);
    initSingleHash(&th->hash3, s, MOD3);
}

/**
 * 获取子串的三模哈希值
 */
void getTripleHash(TripleHash* th, int l, int r, long long result[3]) {
    result[0] = getSingleHash(&th->hash1, l, r);
    result[1] = getSingleHash(&th->hash2, l, r);
    result[2] = getSingleHash(&th->hash3, l, r);
}

/**
 * 比较两个子串是否相等
 */
int tripleHashEquals(TripleHash* th, int l1, int r1, int l2, int r2) {
    long long hash1[3], hash2[3];
    getTripleHash(th, l1, r1, hash1);
    getTripleHash(th, l2, r2, hash2);
    return (hash1[0] == hash2[0] && hash1[1] == hash2[1] && hash1[2] == hash2[2]);
}

/**
 * 持久化前缀哈希
 * 支持历史版本查询的前缀哈希
 */
typedef struct {
    long long hashes[MAX_VERSIONS][MAX_STRING_LENGTH + 1];
    long long powers[MAX_VERSIONS][MAX_STRING_LENGTH + 1];
    int lengths[MAX_VERSIONS];
    long long mod;
    int versionCount;
} PersistentPrefixHash;

/**
 * 初始化持久化前缀哈希
 */
void initPersistentPrefixHash(PersistentPrefixHash* ph, long long mod) {
    ph->mod = mod;
    ph->versionCount = 1;
    
    // 初始化空版本
    ph->hashes[0][0] = 0;
    ph->powers[0][0] = 1;
    ph->lengths[0] = 1;
}

/**
 * 在指定版本后添加字符
 */
int appendToPersistentHash(PersistentPrefixHash* ph, int version, char c) {
    if (ph->versionCount >= MAX_VERSIONS) return -1;
    
    int newVersion = ph->versionCount;
    int prevLength = ph->lengths[version];
    
    // 复制之前的内容
    for (int i = 0; i < prevLength; i++) {
        ph->hashes[newVersion][i] = ph->hashes[version][i];
        ph->powers[newVersion][i] = ph->powers[version][i];
    }
    
    // 计算新添加的字符的哈希
    ph->powers[newVersion][prevLength] = (ph->powers[newVersion][prevLength - 1] * BASE) % ph->mod;
    ph->hashes[newVersion][prevLength] = (ph->hashes[newVersion][prevLength - 1] * BASE + (c - 'a' + 1)) % ph->mod;
    ph->lengths[newVersion] = prevLength + 1;
    
    ph->versionCount++;
    return newVersion;
}

/**
 * 获取指定版本中子串的哈希值
 */
long long getPersistentHash(PersistentPrefixHash* ph, int version, int l, int r) {
    long long result = (ph->hashes[version][r + 1] - ph->hashes[version][l] * ph->powers[version][r - l + 1]) % ph->mod;
    if (result < 0) result += ph->mod;
    return result;
}

/**
 * 计算哈希碰撞概率
 */
double collisionProbability(long long mod, long long n) {
    // 使用生日悖论近似计算
    // P(碰撞) ≈ 1 - e^(-n*(n-1)/(2*mod))
    if (mod <= 0 || n <= 1) return 0.0;
    double exponent = -((double)n * (n - 1)) / (2 * mod);
    // 简化的exp实现
    double exp_val = 1.0;
    double term = 1.0;
    for (int i = 1; i <= 20; i++) {
        term *= exponent / i;
        exp_val += term;
    }
    return 1.0 - exp_val;
}

/**
 * 处理无符号整数溢出
 */
long long handleOverflow(long long value, long long mod) {
    value %= mod;
    if (value < 0) value += mod;
    return value;
}

// 由于环境限制，不包含main函数和输出语句
// 算法核心功能已实现，可被其他程序调用