// AVL树实现（C++版本） - 数组模拟指针实现
// 时间复杂度：所有操作均为O(log n)
// 空间复杂度：O(n)
// 核心功能：插入、删除、查询排名、查询第k小、查询前驱、查询后继
// 作者：算法旅程 - 专注于数据结构与算法的学习与实现
// 版本：v1.0
// 工程化考量：使用数组模拟指针避免动态内存分配的开销和内存泄漏风险
// 实现一种结构，支持如下操作，要求单次调用的时间复杂度O(log n)
// 1，增加x，重复加入算多个词频
// 2，删除x，如果有多个，只删掉一个
// 3，查询x的排名，x的排名为，比x小的数的个数+1
// 4，查询数据中排名为x的数
// 5，查询x的前驱，x的前驱为，小于x的数中最大的数，不存在返回整数最小值
// 6，查询x的后继，x的后继为，大于x的数中最小的数，不存在返回整数最大值
// 所有操作的次数 <= 10^5
// -10^7 <= x <= +10^7
// 测试链接 : https://www.luogu.com.cn/problem/P3369

/*
 * 补充题目列表（分类整理）：
 * 
 * 一、基础模板题（直接应用AVL树实现）
 * 1. 洛谷 P3369 【模板】普通平衡树
 *    链接: https://www.luogu.com.cn/problem/P3369
 *    题目描述: 实现一个普通平衡树，支持插入、删除、查询排名、查询第k小值、查询前驱和后继
 *    时间复杂度: O(log n) 每次操作
 *    空间复杂度: O(n)
 *    核心考点: AVL树的基本操作实现
 *    适用文件: 本实现文件可直接用于本题
 * 
 * 2. 洛谷 P6136 【模板】普通平衡树（数据加强版）
 *    链接: https://www.luogu.com.cn/problem/P6136
 *    题目描述: P3369的数据加强版，强制在线，需要更高的效率和更强的实现
 *    时间复杂度: O(log n) 每次操作
 *    空间复杂度: O(n)
 *    核心考点: 性能优化、在线处理、抗哈希攻击
 *    适用文件: 本实现文件需要优化常数因子以通过
 * 
 * 二、数据结构应用题（AVL树作为工具解决问题）
 * 3. LeetCode 406. Queue Reconstruction by Height
 *    链接: https://leetcode.cn/problems/queue-reconstruction-by-height/
 *    题目描述: 重构队列，每个人有身高和前面比他高的人数要求，需要重构满足条件的队列
 *    时间复杂度: O(n² log n) - 使用AVL树实现可优化至O(n log n)
 *    空间复杂度: O(n)
 *    核心考点: 贪心算法、区间插入、有序维护
 * 
 * 4. LeetCode 220. Contains Duplicate III
 *    链接: https://leetcode.cn/problems/contains-duplicate-iii/
 *    题目描述: 判断数组中是否存在两个不同下标i和j，使得abs(nums[i] - nums[j]) <= t且abs(i - j) <= k
 *    时间复杂度: O(n log k)
 *    空间复杂度: O(k)
 *    核心考点: 滑动窗口、有序集合、前驱后继查询
 *    解题技巧: 维护大小为k的滑动窗口，对于每个元素查询其在窗口中的前驱和后继
 * 
 * 5. Codeforces 459D - Pashmak and Parmida's problem
 *    链接: https://codeforces.com/problems/459/D
 *    题目描述: 计算满足条件的点对数量
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 *    核心考点: 树状数组/平衡树的应用、前缀和思想
 * 
 * 三、AVL树特性应用题（专注于平衡树的平衡性）
 * 6. PAT甲级 1066 Root of AVL Tree
 *    链接: https://pintia.cn/problem-sets/994805342720868352/exam/problems/994805404939173888
 *    题目描述: 给定插入序列，构建AVL树，输出根节点的值
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 *    核心考点: AVL树的构建、旋转操作
 * 
 * 7. PAT甲级 1123 Is It a Complete AVL Tree
 *    链接: https://pintia.cn/problem-sets/994805342720868352/exam/problems/994805355103797248
 *    题目描述: 判断构建的AVL树是否是完全二叉树
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 *    核心考点: AVL树构建、完全二叉树判断（层序遍历）
 * 
 * 四、其他适用题目
 * 8. SPOJ Ada and Behives
 *    链接: https://www.spoj.com/problems/ADAAPHID/
 *    题目描述: 维护一个动态集合，支持插入和查询操作
 *    时间复杂度: O(log n) 每次操作
 *    空间复杂度: O(n)
 *    核心考点: 动态集合维护、区间查询
 * 
 * 9. LeetCode 98. 验证二叉搜索树
 *    链接: https://leetcode.cn/problems/validate-binary-search-tree/
 *    题目描述: 判断一个二叉树是否是有效的二叉搜索树
 *    时间复杂度: O(n)
 *    空间复杂度: O(log n) 递归栈空间
 *    核心考点: 中序遍历、二叉搜索树性质验证
 * 
 * 10. LeetCode 669. 修剪二叉搜索树
 *    链接: https://leetcode.cn/problems/trim-a-binary-search-tree/
 *    题目描述: 裁剪二叉搜索树，保留在[low, high]范围内的节点
 *    时间复杂度: O(n)
 *    空间复杂度: O(log n) 递归栈空间
 *    核心考点: 递归、树的修改操作
 * 
 * 11. HackerRank Self-Balancing Tree
 *    链接: https://www.hackerrank.com/challenges/self-balancing-tree/problem
 *    题目描述: 实现AVL树的插入操作
 *    时间复杂度: O(log n)
 *    空间复杂度: O(n)
 *    核心考点: AVL树节点定义和旋转操作
 * 
 * 12. USACO 2017 December Contest, Platinum Problem 1. Standing Out from the Herd
 *    链接: http://www.usaco.org/index.php?page=viewproblem2&cpid=770
 *    题目描述: 字符串处理问题，可使用平衡树优化
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 *    核心考点: 后缀数组+平衡树
 * 
 * 13. CodeChef ORDERSET
 *    链接: https://www.codechef.com/problems/ORDERSET
 *    题目描述: 维护有序集合，支持插入、删除、查询排名、查询第k小
 *    时间复杂度: O(log n)
 *    空间复杂度: O(n)
 *    核心考点: 平衡树基本操作
 * 
 * 14. AtCoder ABC134 E - Sequence Decomposing
 *    链接: https://atcoder.jp/contests/abc134/tasks/abc134_e
 *    题目描述: 序列分解问题，可使用平衡树优化
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 *    核心考点: LIS变种+平衡树
 * 
 * 15. ZOJ 1659 Mobile Phone Coverage
 *    链接: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827368277
 *    题目描述: 计算矩形覆盖面积，可使用平衡树维护
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 *    核心考点: 扫描线+平衡树
 * 
 * 16. POJ 1864 [NOI2009] 二叉查找树
 *    链接: http://poj.org/problem?id=1864
 *    题目描述: 二叉查找树的动态规划问题
 *    时间复杂度: O(n^2)
 *    空间复杂度: O(n)
 *    核心考点: 树形DP+平衡树
 * 
 * 17. HDU 4589 Special equations
 *    链接: http://acm.hdu.edu.cn/showproblem.php?pid=4589
 *    题目描述: 数学问题，可使用平衡树优化
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 *    核心考点: 数论+平衡树
 * 
 * 算法思路技巧总结（深入解析）：
 * 
 * 1. 适用场景与核心思想
 *    - 数据规模：适用于需要频繁插入/删除/查询的场景，数据量在10^5-10^6级别
 *    - 操作需求：当需要同时支持插入、删除、查询排名、查询第k小等操作时，平衡树是最佳选择
 *    - 核心思想：通过旋转操作维持树的高度平衡，确保所有操作的时间复杂度为O(log n)
 * 
 * 2. 关键操作与实现细节
 *    - 节点信息维护：每个节点需要维护高度、子树大小、键值计数等信息
 *    - 旋转操作实现：四种旋转（LL、RR、LR、RL）是维护平衡性的核心
 *    - 插入删除策略：插入后自底向上更新信息并检查平衡因子
 *    - 重复元素处理：通过计数机制避免节点冗余，提高空间效率
 * 
 * 3. 时间与空间复杂度分析
 *    - 时间复杂度：所有操作均为O(log n)，因为树的高度被严格控制在O(log n)
 *    - 空间复杂度：O(n)，每个节点存储常数个额外信息
 *    - 常数因子分析：AVL树相比红黑树旋转次数更多，但查询更稳定
 * 
 * 4. 工程化考量
 *    - 内存管理：数组实现相比指针实现更高效，避免了动态内存分配的开销
 *    - 边界处理：空节点处理、重复键处理、极值处理是实现稳定的关键
 *    - 性能优化：使用计数处理重复元素、避免不必要的旋转操作
 *    - 扩展性：可以轻松扩展支持更多操作，如区间查询、区间修改等
 * 
 * 5. 语言特性差异与实现选择
 *    - C++：数组实现最高效，适合竞赛环境；指针实现更灵活但开销略大
 *    - Java：对象引用操作直观，但需要注意GC影响；数组模拟也可实现
 *    - Python：递归实现简洁但性能较弱；可以使用类和字典实现节点
 * 
 * 6. 常见错误与调试技巧
 *    - 平衡因子计算错误：确保高度信息正确更新
 *    - 旋转后未更新信息：必须先更新子节点，再更新父节点
 *    - 递归终止条件错误：特别注意空节点的处理
 *    - 调试方法：打印中间状态、使用小测试用例验证
 * 
 * 7. 性能优化方向
 *    - 路径压缩：某些情况下可以缓存中间计算结果
 *    - 批量操作：合并连续的插入删除操作以减少旋转次数
 *    - 内存池：预分配节点空间，避免动态分配
 *    - 并行处理：在支持的场景下可以考虑并行化某些操作
 */

// 简化版C++实现，避免使用STL容器
// MAXN定义节点数组的最大容量，必须足够大以容纳所有节点

const int MAXN = 100001; // 最大节点数，可根据实际需求调整

// 全局变量 - 用于数组模拟指针实现AVL树
int cnt = 0;          // 节点计数器，记录当前已使用的节点数量
int head = 0;         // 根节点索引（0表示空节点）
int key[MAXN];        // 节点键值数组
int height[MAXN];     // 节点高度数组，用于维护平衡性
int ls[MAXN];         // 左子节点索引数组（left son）
int rs[MAXN];         // 右子节点索引数组（right son）
int key_count[MAXN];  // 键值计数数组，处理重复元素
int siz[MAXN];        // 子树大小数组，用于快速计算排名

/**
 * @brief 自定义max函数
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 * 工程优化：避免使用STL的max函数，减少依赖
 * 
 * @param a 第一个整数
 * @param b 第二个整数
 * @return int 较大的整数
 */
int my_max(int a, int b) {
    return a > b ? a : b;
}

/**
 * @brief 自定义min函数
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 * 工程优化：避免使用STL的min函数，减少依赖
 * 
 * @param a 第一个整数
 * @param b 第二个整数
 * @return int 较小的整数
 */
int my_min(int a, int b) {
    return a < b ? a : b;
}

/**
 * @brief 更新节点信息（子树大小和高度）
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 * 核心功能：维护节点的子树大小和高度信息，这是AVL树操作的基础
 * 边界情况：假设传入的节点索引i有效（非空节点）
 * 
 * @param i 需要更新信息的节点索引
 */
void up(int i) {
    // 子树大小 = 左子树大小 + 右子树大小 + 当前节点的计数
    siz[i] = siz[ls[i]] + siz[rs[i]] + key_count[i];
    // 节点高度 = 左右子树最大高度 + 1
    height[i] = my_max(height[ls[i]], height[rs[i]]) + 1;
}

/**
 * @brief 左旋操作 - 处理RR情况
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 * 操作图解:
 *     i                 r
 *    / \               / \
 *   T1  r    ----->    i  T3
 *      / \             / \
 *     T2 T3           T1 T2
 * 
 * @param i 需要旋转的根节点索引
 * @return int 旋转后的新根节点索引
 * 关键点：旋转后必须先更新子节点信息，再更新父节点信息
 */
int leftRotate(int i) {
    int r = rs[i];           // 保存右子节点
    rs[i] = ls[r];           // 右子节点的左子树成为当前节点的右子树
    ls[r] = i;               // 当前节点成为右子节点的左子树
    up(i);                   // 先更新原根节点信息
    up(r);                   // 再更新新根节点信息
    return r;                // 返回新的根节点索引
}

/**
 * @brief 右旋操作 - 处理LL情况
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 * 操作图解:
 *       i                 l
 *      / \               / \
 *     l  T3    ----->   T1  i
 *    / \                   / \
 *   T1 T2                 T2 T3
 * 
 * @param i 需要旋转的根节点索引
 * @return int 旋转后的新根节点索引
 * 关键点：旋转后必须先更新子节点信息，再更新父节点信息
 */
int rightRotate(int i) {
    int l = ls[i];           // 保存左子节点
    ls[i] = rs[l];           // 左子节点的右子树成为当前节点的左子树
    rs[l] = i;               // 当前节点成为左子节点的右子树
    up(i);                   // 先更新原根节点信息
    up(l);                   // 再更新新根节点信息
    return l;                // 返回新的根节点索引
}

/**
 * @brief 维护AVL树的平衡
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 * 核心功能：根据平衡因子判断是否需要旋转，并执行相应的旋转操作
 * 
 * @param i 需要维护平衡的节点索引
 * @return int 维护平衡后的节点索引
 * 处理四种旋转情况：
 * 1. LL情况：左子树的左子树导致失衡 - 单右旋
 * 2. LR情况：左子树的右子树导致失衡 - 先左旋左子树，再右旋当前节点
 * 3. RR情况：右子树的右子树导致失衡 - 单左旋
 * 4. RL情况：右子树的左子树导致失衡 - 先右旋右子树，再左旋当前节点
 */
int maintain(int i) {
    int lh = height[ls[i]];  // 左子树高度
    int rh = height[rs[i]];  // 右子树高度
    
    // 左子树过高，需要右旋处理
    if (lh - rh > 1) {
        // LL情况：左子树的左子树更高
        if (height[ls[ls[i]]] >= height[rs[ls[i]]]) {
            i = rightRotate(i);
        } else {
            // LR情况：左子树的右子树更高
            ls[i] = leftRotate(ls[i]);
            i = rightRotate(i);
        }
    } 
    // 右子树过高，需要左旋处理
    else if (rh - lh > 1) {
        // RR情况：右子树的右子树更高
        if (height[rs[rs[i]]] >= height[ls[rs[i]]]) {
            i = leftRotate(i);
        } else {
            // RL情况：右子树的左子树更高
            rs[i] = rightRotate(rs[i]);
            i = leftRotate(i);
        }
    }
    
    return i;
}

/**
 * @brief 插入节点的递归实现
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n) - 递归栈空间
 * 算法步骤：
 * 1. 递归终止条件：到达空节点位置，创建新节点
 * 2. 键值相等：增加计数（优化处理重复元素）
 * 3. 键值小于当前节点：递归插入左子树
 * 4. 键值大于当前节点：递归插入右子树
 * 5. 更新节点信息并维护平衡
 * 
 * @param i 当前子树根节点索引
 * @param num 要插入的键值
 * @return int 插入后更新的子树根节点索引
 */
int add(int i, int num) {
    // 空节点处理：创建新节点
    if (i == 0) {
        key[++cnt] = num;       // 分配新节点并设置键值
        key_count[cnt] = 1;     // 初始计数为1
        siz[cnt] = 1;           // 初始子树大小为1
        height[cnt] = 1;        // 初始高度为1
        return cnt;             // 返回新节点索引
    }
    
    // 键值相等：增加计数（优化处理重复元素）
    if (key[i] == num) {
        key_count[i]++;
    } 
    // 键值小于当前节点：插入左子树
    else if (key[i] > num) {
        ls[i] = add(ls[i], num);
    } 
    // 键值大于当前节点：插入右子树
    else {
        rs[i] = add(rs[i], num);
    }
    
    up(i);              // 更新当前节点信息
    return maintain(i); // 维护平衡并返回更新后的根节点
}

/**
 * @brief 公共接口：插入键值
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n) - 递归栈空间
 * 
 * @param num 要插入的键值
 */
void add_num(int num) {
    head = add(head, num);
}

/**
 * @brief 计算键值num的排名（比num小的数的个数）
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n) - 递归栈空间
 * 算法核心：利用子树大小进行快速排名计算
 * 
 * @param i 当前子树根节点索引
 * @param num 要查询排名的键值
 * @return int 比num小的数的个数
 */
int getRank(int i, int num) {
    // 空节点返回0（没有比num小的数）
    if (i == 0) {
        return 0;
    }
    
    // 当前节点键值大于等于num，在左子树中查找
    if (key[i] >= num) {
        return getRank(ls[i], num);
    } 
    // 当前节点键值小于num，排名 = 左子树大小 + 当前节点计数 + 右子树中的排名
    else {
        return siz[ls[i]] + key_count[i] + getRank(rs[i], num);
    }
}

/**
 * @brief 公共接口：获取键值的排名（从1开始）
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n) - 递归栈空间
 * 
 * @param num 要查询排名的键值
 * @return int 键值的排名（比num小的数的个数+1）
 */
int get_rank(int num) {
    // 调用getRank获取比num小的数的个数，加1得到排名
    return getRank(head, num) + 1;
}

/**
 * @brief 删除子树中的最左节点（中序遍历第一个节点）
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n) - 递归栈空间
 * 用途：用于删除操作中寻找和删除中序后继
 * 
 * @param i 当前子树根节点索引
 * @param mostLeft 要删除的最左节点索引
 * @return int 删除后的子树根节点索引
 */
int removeMostLeft(int i, int mostLeft) {
    // 找到要删除的最左节点
    if (i == mostLeft) {
        // 返回其右子树作为替代
        return rs[i];
    } else {
        // 递归删除左子树中的最左节点
        ls[i] = removeMostLeft(ls[i], mostLeft);
        up(i);              // 更新信息
        return maintain(i); // 维护平衡
    }
}

/**
 * @brief 删除节点的递归实现
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n) - 递归栈空间
 * 算法步骤：
 * 1. 找到要删除的节点
 * 2. 如果有重复元素，减少计数即可
 * 3. 如果只有一个子节点或无子节点，直接替换
 * 4. 如果有两个子节点，找到后继节点并替换
 * 5. 更新节点信息并维护平衡
 * 
 * @param i 当前子树根节点索引
 * @param num 要删除的键值
 * @return int 删除后的子树根节点索引
 */
int remove_node(int i, int num) {
    // 键值小于当前节点：在右子树中删除
    if (key[i] < num) {
        rs[i] = remove_node(rs[i], num);
    }
    // 键值大于当前节点：在左子树中删除
    else if (key[i] > num) {
        ls[i] = remove_node(ls[i], num);
    }
    // 找到要删除的节点
    else {
        // 情况1：如果有重复元素，减少计数即可
        if (key_count[i] > 1) {
            key_count[i]--;
        }
        // 情况2：处理节点删除
        else {
            // 子情况1：叶子节点（无子节点）
            if (ls[i] == 0 && rs[i] == 0) {
                return 0; // 返回空节点
            }
            // 子情况2：只有左子节点
            else if (ls[i] != 0 && rs[i] == 0) {
                i = ls[i]; // 用左子节点替换
            }
            // 子情况3：只有右子节点
            else if (ls[i] == 0 && rs[i] != 0) {
                i = rs[i]; // 用右子节点替换
            }
            // 子情况4：有两个子节点
            else {
                // 找到右子树中的最左节点（中序后继）
                int mostLeft = rs[i];
                while (ls[mostLeft] != 0) {
                    mostLeft = ls[mostLeft];
                }
                // 删除后继节点
                rs[i] = removeMostLeft(rs[i], mostLeft);
                // 用后继节点替换当前节点
                ls[mostLeft] = ls[i];
                rs[mostLeft] = rs[i];
                i = mostLeft;
            }
        }
    }
    
    up(i);              // 更新节点信息
    return maintain(i); // 维护平衡
}

/**
 * @brief 公共接口：删除键值
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n) - 递归栈空间
 * 优化：只有当键值存在时才执行删除操作
 * 
 * @param num 要删除的键值
 */
void remove_num(int num) {
    // 判断键值是否存在：如果num的排名等于num+1的排名，说明num不存在
    if (get_rank(num) != get_rank(num + 1)) {
        head = remove_node(head, num);
    }
}

/**
 * @brief 查询排名为x的键值
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n) - 递归栈空间
 * 算法核心：利用子树大小快速定位第k小元素
 * 
 * @param i 当前子树根节点索引
 * @param x 要查询的排名（从1开始）
 * @return int 第x小的键值
 */
int index_node(int i, int x) {
    // 如果左子树大小大于等于x，第x小的元素在左子树中
    if (siz[ls[i]] >= x) {
        return index_node(ls[i], x);
    }
    // 如果左子树大小+当前节点计数小于x，第x小的元素在右子树中
    else if (siz[ls[i]] + key_count[i] < x) {
        // 调整x值，减去左子树和当前节点的数量
        return index_node(rs[i], x - siz[ls[i]] - key_count[i]);
    }
    // 否则，当前节点就是第x小的元素
    return key[i];
}

/**
 * @brief 公共接口：获取第x小的键值
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n) - 递归栈空间
 * 
 * @param x 要查询的排名（从1开始）
 * @return int 第x小的键值
 */
int get_index(int x) {
    return index_node(head, x);
}

/**
 * @brief 查询num的前驱（小于num的最大数）
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n) - 递归栈空间
 * 算法思路：
 * - 如果当前节点键值大于等于num，前驱一定在左子树
 * - 如果当前节点键值小于num，当前节点可能是前驱，或前驱在右子树中
 * 
 * @param i 当前子树根节点索引
 * @param num 要查询前驱的键值
 * @return int num的前驱，如果不存在返回INT_MIN
 */
int pre_node(int i, int num) {
    // 空节点返回INT_MIN（表示不存在前驱）
    if (i == 0) {
        return -2147483647;  // INT_MIN
    }
    
    // 当前节点键值大于等于num，前驱一定在左子树
    if (key[i] >= num) {
        return pre_node(ls[i], num);
    }
    // 当前节点键值小于num，在右子树中寻找更大的可能前驱
    else {
        // 取当前节点和右子树中找到的前驱的最大值
        return my_max(key[i], pre_node(rs[i], num));
    }
}

/**
 * @brief 公共接口：获取键值的前驱
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n) - 递归栈空间
 * 
 * @param num 要查询前驱的键值
 * @return int num的前驱，如果不存在返回INT_MIN
 */
int get_pre(int num) {
    return pre_node(head, num);
}

/**
 * @brief 查询num的后继（大于num的最小数）
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n) - 递归栈空间
 * 算法思路：
 * - 如果当前节点键值小于等于num，后继一定在右子树
 * - 如果当前节点键值大于num，当前节点可能是后继，或后继在左子树中
 * 
 * @param i 当前子树根节点索引
 * @param num 要查询后继的键值
 * @return int num的后继，如果不存在返回INT_MAX
 */
int post_node(int i, int num) {
    // 空节点返回INT_MAX（表示不存在后继）
    if (i == 0) {
        return 2147483647;  // INT_MAX
    }
    
    // 当前节点键值小于等于num，后继一定在右子树
    if (key[i] <= num) {
        return post_node(rs[i], num);
    }
    // 当前节点键值大于num，在左子树中寻找更小的可能后继
    else {
        // 取当前节点和左子树中找到的后继的最小值
        return my_min(key[i], post_node(ls[i], num));
    }
}

/**
 * @brief 公共接口：获取键值的后继
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n) - 递归栈空间
 * 
 * @param num 要查询后继的键值
 * @return int num的后继，如果不存在返回INT_MAX
 */
int get_post(int num) {
    return post_node(head, num);
}

/**
 * @brief 清理AVL树
 * 时间复杂度：O(cnt) - cnt为当前节点数量
 * 空间复杂度：O(1)
 * 工程注意事项：
 * - 这是一个简化的清理实现，将所有节点信息重置
 * - 在实际应用中，可能需要更复杂的内存管理策略
 * - 在数组实现中，我们只是重置了索引和值，并没有释放内存（因为使用的是静态数组）
 */
void clear_tree() {
    // 重置所有已使用节点的信息
    for (int i = 1; i <= cnt; i++) {
        key[i] = 0;
        height[i] = 0;
        ls[i] = 0;
        rs[i] = 0;
        key_count[i] = 0;
        siz[i] = 0;
    }
    // 重置节点计数器和根节点
    cnt = 0;
    head = 0;
}

/**
 * @brief 主函数示例（为了兼容不同编译环境，此处注释掉）
 * 以下是一个标准的主函数实现示例，可根据实际需要启用
 * 
#include <iostream>
using namespace std;

int main() {
    // 初始化已通过全局变量的初始值完成
    
    int n, m;
    cin >> n >> m;
    
    // 插入初始数据
    for (int i = 0; i < n; ++i) {
        int x;
        cin >> x;
        add_num(x);
    }
    
    // 处理查询操作
    int lastAns = 0; // 上一次查询的答案，用于处理带偏移的查询
    for (int i = 0; i < m; ++i) {
        int op, x;
        cin >> op >> x;
        x ^= lastAns; // 异或上一次答案，处理偏移（洛谷P3369的在线处理要求）
        
        switch (op) {
            case 1: // 插入
                add_num(x);
                break;
            case 2: // 删除
                remove_num(x);
                break;
            case 3: // 查询排名
                lastAns = get_rank(x);
                cout << lastAns << endl;
                break;
            case 4: // 查询第k小
                lastAns = get_index(x);
                cout << lastAns << endl;
                break;
            case 5: // 查询前驱
                lastAns = get_pre(x);
                cout << lastAns << endl;
                break;
            case 6: // 查询后继
                lastAns = get_post(x);
                cout << lastAns << endl;
                break;
        }
    }
    
    return 0;
}
*/

// 注意：在实际应用中，请根据具体的编译环境和要求，添加适当的main函数
// 洛谷P3369和P6136题目需要完整的输入输出处理，以及在线查询支持

/**
 * @brief 验证AVL树是否满足BST性质
 * 时间复杂度：O(n)
 * 空间复杂度：O(log n) - 递归栈空间
 * 工程用途：调试和验证AVL树实现的正确性
 * 
 * @param i 当前子树根节点索引
 * @param min_val 允许的最小值
 * @param max_val 允许的最大值
 * @return bool 是否满足BST性质
 */
bool isValidBST(int i, int min_val, int max_val) {
    if (i == 0) return true;
    
    // 检查当前节点值是否在允许范围内
    if (key[i] <= min_val || key[i] >= max_val) {
        return false;
    }
    
    // 递归检查左右子树
    return isValidBST(ls[i], min_val, key[i]) && 
           isValidBST(rs[i], key[i], max_val);
}

/**
 * @brief 验证AVL树是否平衡
 * 时间复杂度：O(n)
 * 空间复杂度：O(log n) - 递归栈空间
 * 工程用途：调试和验证AVL树平衡性
 * 
 * @param i 当前子树根节点索引
 * @return bool 是否平衡
 */
bool isBalanced(int i) {
    if (i == 0) return true;
    
    int lh = height[ls[i]];
    int rh = height[rs[i]];
    
    // 检查当前节点平衡因子
    if ((lh > rh ? lh - rh : rh - lh) > 1) {
        return false;
    }
    
    // 递归检查左右子树
    return isBalanced(ls[i]) && isBalanced(rs[i]);
}

/**
 * @brief 获取树的高度
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 * 
 * @return int 树的高度
 */
int getTreeHeight() {
    return height[head];
}

/**
 * @brief 获取树的总大小（元素个数，包括重复元素）
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 * 
 * @return int 树中元素的总个数
 */
int getTotalSize() {
    return siz[head];
}

/**
 * @brief 获取不同元素的个数（去重后）
 * 时间复杂度：O(n)
 * 空间复杂度：O(log n) - 递归栈空间
 * 
 * @param i 当前子树根节点索引
 * @return int 不同元素的个数
 */
int getDistinctCount(int i) {
    if (i == 0) return 0;
    return 1 + getDistinctCount(ls[i]) + getDistinctCount(rs[i]);
}

/**
 * @brief 中序遍历打印树（用于调试）
 * 时间复杂度：O(n)
 * 空间复杂度：O(log n) - 递归栈空间
 * 
 * @param i 当前子树根节点索引
 */
void inorderTraversal(int i) {
    if (i == 0) return;
    
    inorderTraversal(ls[i]);
    std::cout << key[i] << "(" << key_count[i] << ") ";
    inorderTraversal(rs[i]);
}

/**
 * @brief 性能测试：插入大量随机数据
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 * 工程用途：测试AVL树在大规模数据下的性能表现
 * 
 * @param n 要插入的数据量
 */
void performanceTest(int n) {
    clear_tree();
    
    std::cout << "开始性能测试，插入 " << n << " 个随机数据..." << std::endl;
    
    // 插入随机数据
    for (int i = 0; i < n; ++i) {
        int num = rand() % 1000000;
        add_num(num);
    }
    
    std::cout << "插入完成，树高度: " << getTreeHeight() << std::endl;
    std::cout << "总元素个数: " << getTotalSize() << std::endl;
    std::cout << "不同元素个数: " << getDistinctCount(head) << std::endl;
    
    // 验证BST性质
    if (isValidBST(head, -2147483647, 2147483647)) {
        std::cout << "BST性质验证通过" << std::endl;
    } else {
        std::cout << "BST性质验证失败" << std::endl;
    }
    
    // 验证平衡性
    if (isBalanced(head)) {
        std::cout << "平衡性验证通过" << std::endl;
    } else {
        std::cout << "平衡性验证失败" << std::endl;
    }
}

/**
 * @brief 测试用例：基本功能测试
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 */
void basicTest() {
    clear_tree();
    
    std::cout << "=== 基本功能测试 ===" << std::endl;
    
    // 插入测试
    add_num(10);
    add_num(20);
    add_num(30);
    add_num(40);
    add_num(50);
    add_num(25);
    
    std::cout << "插入10,20,30,40,50,25后:" << std::endl;
    std::cout << "30的排名: " << get_rank(30) << std::endl;
    std::cout << "第3小的数: " << get_index(3) << std::endl;
    std::cout << "25的前驱: " << get_pre(25) << std::endl;
    std::cout << "25的后继: " << get_post(25) << std::endl;
    
    // 删除测试
    remove_num(30);
    std::cout << "删除30后:" << std::endl;
    std::cout << "30的排名: " << get_rank(30) << std::endl;
    std::cout << "第3小的数: " << get_index(3) << std::endl;
    
    // 中序遍历验证
    std::cout << "中序遍历: ";
    inorderTraversal(head);
    std::cout << std::endl;
}

/**
 * @brief 测试用例：重复元素测试
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 */
void duplicateTest() {
    clear_tree();
    
    std::cout << "=== 重复元素测试 ===" << std::endl;
    
    // 插入重复元素
    add_num(10);
    add_num(10);
    add_num(10);
    add_num(20);
    add_num(20);
    
    std::cout << "插入3个10和2个20后:" << std::endl;
    std::cout << "10的排名: " << get_rank(10) << std::endl;
    std::cout << "11的排名: " << get_rank(11) << std::endl;
    std::cout << "第1小的数: " << get_index(1) << std::endl;
    std::cout << "第3小的数: " << get_index(3) << std::endl;
    std::cout << "第5小的数: " << get_index(5) << std::endl;
    
    // 删除一个10
    remove_num(10);
    std::cout << "删除一个10后:" << std::endl;
    std::cout << "10的排名: " << get_rank(10) << std::endl;
    std::cout << "第3小的数: " << get_index(3) << std::endl;
}

/**
 * @brief 测试用例：边界情况测试
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 */
void edgeCaseTest() {
    clear_tree();
    
    std::cout << "=== 边界情况测试 ===" << std::endl;
    
    // 空树测试
    std::cout << "空树测试:" << std::endl;
    std::cout << "1的排名: " << get_rank(1) << std::endl;
    std::cout << "第1小的数: " << get_index(1) << std::endl;
    std::cout << "1的前驱: " << get_pre(1) << std::endl;
    std::cout << "1的后继: " << get_post(1) << std::endl;
    
    // 单节点测试
    add_num(100);
    std::cout << "单节点测试:" << std::endl;
    std::cout << "50的排名: " << get_rank(50) << std::endl;
    std::cout << "100的排名: " << get_rank(100) << std::endl;
    std::cout << "150的排名: " << get_rank(150) << std::endl;
    std::cout << "100的前驱: " << get_pre(100) << std::endl;
    std::cout << "100的后继: " << get_post(100) << std::endl;
    
    // 极值测试
    add_num(-1000000);
    add_num(1000000);
    std::cout << "极值测试:" << std::endl;
    std::cout << "-1000000的前驱: " << get_pre(-1000000) << std::endl;
    std::cout << "1000000的后继: " << get_post(1000000) << std::endl;
}

/**
 * @brief 主函数：测试驱动
 * 编译命令：g++ -std=c++11 -O2 Code01_AVL.cpp -o avl_test
 * 运行命令：./avl_test
 */
/*
int main() {
    // 设置随机种子
    srand(time(0));
    
    // 运行测试用例
    basicTest();
    std::cout << std::endl;
    
    duplicateTest();
    std::cout << std::endl;
    
    edgeCaseTest();
    std::cout << std::endl;
    
    // 性能测试
    performanceTest(10000);
    std::cout << std::endl;
    
    performanceTest(100000);
    
    return 0;
}
*/

// 注意：在实际应用中，请根据具体的编译环境和要求，添加适当的main函数
// 洛谷P3369和P6136题目需要完整的输入输出处理，以及在线查询支持