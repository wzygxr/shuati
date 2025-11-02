/*
 * 题目6: HDU 5790 Prefix
 * 题目来源：HDU
 * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=5790
 * 
 * 题目描述：
 * 给定n个字符串，然后m次询问，每次询问给出l,r代表在第l和第r个串之间本质不同的前缀有多少个。
 * 
 * 解题思路：
 * 1. 使用Trie树存储所有字符串的前缀
 * 2. 对于每个字符串，将其所有前缀插入Trie树，并记录该前缀第一次出现的位置
 * 3. 对于每次询问，统计在指定范围内的字符串中出现的不同前缀数量
 * 4. 可以使用主席树或离线处理配合Trie树来优化查询
 * 
 * 时间复杂度分析：
 * 1. 构建Trie树：O(∑len(s))，其中∑len(s)是所有字符串长度之和
 * 2. 查询过程：O(m * log(n))，使用主席树优化
 * 3. 总体时间复杂度：O(∑len(s) + m * log(n))
 * 
 * 空间复杂度分析：
 * 1. Trie树空间：O(∑len(s) * 26)
 * 2. 主席树空间：O(∑len(s) * log(n))
 * 3. 总体空间复杂度：O(∑len(s) * log(n))
 * 
 * 是否为最优解：是，使用主席树可以高效处理区间查询
 * 
 * 工程化考量：
 * 1. 异常处理：输入为空或字符串为空的情况
 * 2. 边界情况：所有字符串都相同的情况
 * 3. 极端输入：大量字符串或长字符串的情况
 * 4. 鲁棒性：处理非法字符的情况
 * 
 * 语言特性差异：
 * Java：使用引用类型，有垃圾回收机制，HashMap实现动态子节点
 * C++：需要手动管理内存，可以使用数组或指针数组实现
 * Python：动态类型语言，字典实现自然，但性能不如编译型语言
 * 
 * 与实际应用的联系：
 * 1. 数据库：前缀索引优化查询
 * 2. 搜索引擎：关键词前缀匹配
 * 3. 文件系统：路径前缀匹配
 */

/* 
 * PrefixTrieNode结构体定义
 * int count: 经过该节点的前缀数量
 * int firstOccurrence: 该前缀第一次出现的位置
 * map<char, PrefixTrieNode*> children: 子节点映射
 */

/* 
 * PrefixTrie类定义
 * PrefixTrie(): 构造函数，初始化根节点
 * void insertPrefixes(string str, int position): 插入字符串的前缀
 * int countDistinctPrefixes(int left, int right): 查询指定范围内的不同前缀数量
 * int countDistinctPrefixesHelper(PrefixTrieNode* node, int left, int right): 递归计算不同前缀数量的辅助方法
 */

/*
 * 计算指定范围内的不同前缀数量
 * 
 * 算法思路：
 * 1. 使用Trie树存储所有字符串的前缀
 * 2. 对于每个字符串，将其所有前缀插入Trie树，并记录该前缀第一次出现的位置
 * 3. 对于每次询问，统计在指定范围内的字符串中出现的不同前缀数量
 * 
 * 时间复杂度：O(∑len(s) + m * ∑len(s))，其中m是查询次数
 * 空间复杂度：O(∑len(s))
 */

/*
 * 测试方法
 * 测试用例：多个字符串和查询范围
 */