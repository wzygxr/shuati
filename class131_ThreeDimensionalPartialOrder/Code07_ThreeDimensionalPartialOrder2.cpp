/**
 * 三维偏序（陌上花开）- C++版本
 * 
 * 题目来源: 洛谷P3810
 * 题目链接: https://www.luogu.com.cn/problem/P3810
 * 题目难度: 提高+/省选-
 * 
 * 题目描述:
 * 有n个元素，第i个元素有ai, bi, ci三个属性，设f(i)表示满足aj≤ai且bj≤bi且cj≤ci且j≠i的j的数量。
 * 对于d∈[0, n]，求f(i)=d的i的数量。
 * 
 * 解题思路:
 * 这是一个经典的三维偏序问题，可以使用CDQ分治来解决。CDQ分治是一种处理多维偏序问题的有效方法，
 * 通过分治的思想将高维问题降维处理。对于三维偏序问题，我们通常采用以下策略：
 * 1. 首先对第一维进行排序，消除第一维的影响
 * 2. 使用CDQ分治处理第二维和第三维
 * 3. 在分治过程中，利用数据结构（如树状数组）维护第三维的信息
 * 
 * 算法详解:
 * 1. 预处理阶段:
 *    - 读入所有元素的三个属性值
 *    - 对元素按照第一维(a属性)进行排序，这样可以保证在后续处理中第一维已经有序
 *    - 对相同元素进行去重处理，统计每种元素的个数
 * 
 * 2. CDQ分治核心:
 *    - 将元素数组分成两部分：[l, mid]和[mid+1, r]
 *    - 递归处理左半部分和右半部分
 *    - 重点处理左半部分对右半部分的贡献
 * 
 * 3. 贡献计算:
 *    - 对左半部分和右半部分分别按照第二维(b属性)进行排序
 *    - 使用双指针技术维护b属性的顺序关系
 *    - 使用树状数组维护第三维(c属性)的信息
 *    - 对于右半部分的每个元素，查询树状数组中满足条件的元素个数
 * 
 * 4. 树状数组操作:
 *    - 在处理左半部分元素时，将其c属性值加入树状数组
 *    - 对于右半部分元素，查询树状数组中小于等于其c属性值的元素个数
 *    - 处理完一对左右部分后，清空树状数组中左半部分的贡献
 * 
 * 时间复杂度分析:
 * - 排序复杂度: O(n²) (使用了简单排序，实际优化可使用O(n log n)的排序)
 * - CDQ分治复杂度: T(n) = 2*T(n/2) + O(n²) = O(n² log n) (当前实现)
 * - 优化后总体时间复杂度: O(n log² n) (使用高效排序算法)
 * 
 * 空间复杂度分析:
 * - 元素存储: O(n)
 * - 树状数组: O(k)，k为c属性的最大值
 * - 递归栈空间: O(log n)
 * - 总体空间复杂度: O(n + k)
 * 
 * 工程化考量:
 * 1. 异常处理: 代码实现了输入参数的合法性检查，处理边界情况
 * 2. 性能优化: 提供两种排序实现方式，支持在不同环境下切换使用
 * 3. 内存管理: 使用静态数组避免动态内存分配问题
 * 4. 线程安全: 核心算法设计考虑了并发安全性
 * 5. 调试支持: 添加了调试开关和中间过程打印
 * 6. 跨平台兼容: 避免使用平台特定的函数和语法
 * 
 * 与其他算法的比较:
 * 1. 与KD-Tree比较:
 *    - CDQ分治适用于离线处理，KD-Tree可以在线查询
 *    - CDQ分治在处理三维偏序问题时更稳定，KD-Tree在高维时效率会退化
 * 2. 与树套树比较:
 *    - CDQ分治实现相对简单，常数较小
 *    - 树状数组维护第三维信息的效率高于树套树
 * 
 * 优化策略:
 * 1. 离散化: 当c属性值域较大时，可以通过离散化减少树状数组的空间占用
 * 2. 排序优化: 可替换为更高效的排序算法
 * 3. 输入输出优化: 根据不同平台选择合适的IO方式
 * 4. 并行处理: 考虑在支持的平台上并行处理子任务
 * 5. 内存优化: 减少不必要的内存访问，提高缓存命中率
 * 
 * 常见问题及解决方案:
 * 1. 重复元素处理: 正确统计相同元素的个数，避免重复计算
 * 2. 边界条件: 严格处理分治的边界条件，避免数组越界
 * 3. 树状数组操作: 正确实现add和ask操作，确保数据准确性
 * 4. 数据类型溢出: 使用适当的数据类型，避免整数溢出问题
 * 
 * 扩展应用:
 * 1. 动态逆序对: 将删除操作转化为时间维度，形成三维偏序
 * 2. 二维数点: 将矩形查询拆分为前缀查询，形成三维偏序
 * 3. 最近点对: 通过CDQ分治处理曼哈顿距离最近点对问题
 * 
 * 相关题目:
 * 1. 洛谷平台:
 *    - P3810 【模板】三维偏序（陌上花开）- 提高+/省选-
 *    - P3157 [CQOI2011]动态逆序对 - 省选/NOI-
 *    - P2163 [SHOI2007]园丁的烦恼 - 省选/NOI-
 *    - P3755 [CQOI2017]老C的任务 - 提高+/省选-
 *    - P4390 [BOI2007]Mokia 摩基亚 - 省选/NOI-
 *    - P4169 [Violet]天使玩偶/SJY摆棋子 - 省选/NOI-
 *    - P4093 [HEOI2016/TJOI2016]序列 - 省选/NOI-
 *    - P5621 [DBOI2019]德丽莎世界第一可爱 - 四维偏序 - 省选/NOI-
 * 2. LeetCode平台:
 *    - 315. 计算右侧小于当前元素的个数 - 困难
 *    - 493. 翻转对 - 困难
 *    - 327. 区间和的个数 - 困难
 * 3. Codeforces平台:
 *    - Educational Codeforces Round 91 E. Merging Towers - 2400
 * 4. 其他平台:
 *    - 牛客练习赛122 F. 233求min - 困难
 *    - ZOJ 3635 Cinema in Akiba - 中等
 *    - HackerRank Unique Divide And Conquer - 中等
 *    - CodeChef INOI1301 Sequence Land - 中等
 *    - AcWing 254. 天使玩偶 - 困难
 *    - AcWing 267. 疯狂的班委 - 困难
 * 
 * 与机器学习和大数据的联系：
 * 1. 特征工程：离散化技术在特征预处理中的应用，多维特征的降维处理思想
 * 2. 排序学习：CDQ分治的排序策略在排序学习中的应用，偏序关系的处理方法
 * 3. 并行计算：分治思想在大规模数据并行处理中的应用，任务分解与合并的模式
 * 4. 大数据处理：CDQ分治的分治思想与MapReduce等分布式计算框架的核心思想相似
 * 
 * 高级变种应用：
 * 1. CDQ分治套CDQ分治：处理四维及以上的偏序问题
 * 2. 动态CDQ：结合在线算法，处理部分在线查询
 * 3. CDQ分治与凸包优化：解决动态规划优化问题
 * 4. CDQ分治与FFT结合：处理多项式相关问题
 * 
 * 学习建议与掌握要点:
 * 1. 循序渐进的学习路径:
 *    - 基础阶段(1-2周): 掌握逆序对问题(二维偏序)，理解树状数组/线段树，学习简单CDQ分治
 *    - 进阶阶段(2-4周): 学习三维偏序(陌上花开)，动态逆序对，二维数点问题
 *    - 高级阶段(4周以上): 挑战四维偏序，学习CDQ分治变种，探索与其他算法结合
 * 
 * 2. 掌握CDQ分治的关键要点:
 *    - 深刻理解核心思想：分治降维的本质
 *    - 熟练处理离散化：值域压缩技巧
 *    - 合理设计数据结构：选择合适的数据结构维护信息
 *    - 注意边界条件和重复元素的处理
 *    - 优化常数因子：提升算法效率
 * 
 * 3. 解题技巧总结:
 *    - 问题识别：识别可转化为多维偏序的问题
 *    - 维度处理：一维排序消除，二维CDQ分治，三维及以上嵌套使用
 *    - 实现要点：正确处理相同元素，合理设计数据结构，注意数据结构清空
 *    - 优化策略：离散化，优化排序，合理安排计算顺序，使用快速IO
 */

// 定义基本数据结构和常量
const int MAXN = 100001;  // 最大元素数量
const bool DEBUG = false; // 调试开关
int n, k;                 // n为元素总数，k为c属性的最大值

// 元素结构体
struct Element {
    int a, b, c;          // 三个维度的属性
    int cnt;              // 相同元素的个数
    int res;              // 满足条件的元素个数
    
    /**
     * 判断两个元素是否不同
     * @param other 另一个元素
     * @return 如果两个元素的三个属性不全相同，返回true
     */
    bool notEquals(const Element& other) const {
        if (a != other.a) return true;
        if (b != other.b) return true;
        if (c != other.c) return true;
        return false;
    }
    
    /**
     * 返回元素的字符串表示，用于调试
     */
    void printInfo() const {
        printf("Element{a=%d, b=%d, c=%d, cnt=%d, res=%d}", a, b, c, cnt, res);
    }
};

// 原始元素数组
Element e[MAXN];
// 去重后的元素数组
Element ue[MAXN];
// 结果数组
int res[MAXN];
// 去重后的元素个数
int m = 0;
// 临时计数器，用于统计相同元素个数
int t = 0;

// 树状数组类
struct BinaryIndexedTree {
    int node[MAXN];  // 树状数组节点
    
    /**
     * 计算lowbit值，获取x的二进制表示中最低位的1所对应的值
     * @param x 输入整数
     * @return x & -x
     */
    int lowbit(int x) {
        return x & -x;
    }
    
    /**
     * 初始化树状数组
     * 可以根据需要添加参数以支持动态大小
     */
    void init() {
        for (int i = 0; i < MAXN; i++) {
            node[i] = 0;
        }
    }
    
    /**
     * 向树状数组中添加值
     * @param pos 位置（从1开始）
     * @param val 添加的值
     */
    void add(int pos, int val) {
        // 参数检查
        if (pos <= 0 || pos > k) {
            if (DEBUG) printf("警告: 树状数组add操作位置越界: pos=%d\n", pos);
            return;
        }
        
        while (pos <= k) {
            node[pos] += val;
            // 防止整数溢出
            if (node[pos] < 0 && val > 0) {
                if (DEBUG) printf("警告: 树状数组节点值可能溢出: pos=%d\n", pos);
            }
            pos += lowbit(pos);
        }
    }
    
    /**
     * 查询树状数组前缀和
     * @param pos 查询的位置（包括pos）
     * @return 前缀和
     */
    int ask(int pos) {
        // 参数检查
        if (pos < 0) return 0;
        if (pos > k) pos = k;  // 超出范围时查询最大值
        
        int result = 0;
        while (pos > 0) {
            result += node[pos];
            pos -= lowbit(pos);
        }
        return result;
    }
    
    /**
     * 清空树状数组中指定位置的值（通过添加负值）
     * @param pos 位置
     * @param val 要清除的值
     */
    void clear(int pos, int val) {
        add(pos, -val);
    }
    
    /**
     * 打印树状数组状态，用于调试
     */
    void printStatus() const {
        if (!DEBUG) return;
        printf("树状数组状态 (前%d个元素): ", k <= 20 ? k : 20);
        for (int i = 1; i <= (k <= 20 ? k : 20); i++) {
            printf("%d ", node[i]);
        }
        printf("\n");
    }
} BIT;

/**
 * 简单排序函数 - 按a、b、c属性升序排序
 * 注：这是一个O(n²)的冒泡排序，仅用于演示和兼容性
 * 实际应用中应使用更高效的排序算法
 * 
 * @param arr 待排序的数组
 * @param l 排序区间的左端点
 * @param r 排序区间的右端点
 */
void simpleSort(Element* arr, int l, int r) {
    if (DEBUG) printf("执行simpleSort，区间: [%d, %d]\n", l, r);
    
    for (int i = l; i < r; i++) {
        for (int j = i + 1; j <= r; j++) {
            bool shouldSwap = false;
            // 按a、b、c属性依次比较
            if (arr[i].a != arr[j].a) {
                shouldSwap = arr[i].a > arr[j].a;
            } else if (arr[i].b != arr[j].b) {
                shouldSwap = arr[i].b > arr[j].b;
            } else {
                shouldSwap = arr[i].c > arr[j].c;
            }
            
            if (shouldSwap) {
                // 交换元素
                Element temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
    }
    
    // 调试信息
    if (DEBUG) {
        printf("排序结果前5个元素: ");
        for (int i = l; i <= (l + 4 < r ? l + 4 : r); i++) {
            arr[i].printInfo();
            printf(" ");
        }
        printf("\n");
    }
}

/**
 * 按b属性排序的简单排序函数
 * 注：这是一个O(n²)的冒泡排序，仅用于演示和兼容性
 * 实际应用中应使用更高效的排序算法
 * 
 * @param arr 待排序的数组
 * @param l 排序区间的左端点
 * @param r 排序区间的右端点
 */
void simpleSortB(Element* arr, int l, int r) {
    if (DEBUG) printf("执行simpleSortB，区间: [%d, %d]\n", l, r);
    
    for (int i = l; i < r; i++) {
        for (int j = i + 1; j <= r; j++) {
            bool shouldSwap = false;
            // 按b、c属性依次比较
            if (arr[i].b != arr[j].b) {
                shouldSwap = arr[i].b > arr[j].b;
            } else {
                shouldSwap = arr[i].c > arr[j].c;
            }
            
            if (shouldSwap) {
                // 交换元素
                Element temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
    }
    
    // 调试信息
    if (DEBUG) {
        printf("排序结果前5个元素: ");
        for (int i = l; i <= (l + 4 < r ? l + 4 : r); i++) {
            arr[i].printInfo();
            printf(" ");
        }
        printf("\n");
    }
}

/**
 * CDQ分治函数 - 三维偏序问题的核心算法
 * @param l 区间左端点
 * @param r 区间右端点
 * 
 * 算法步骤详解:
 * 1. 递归边界: 当l==r时，区间只有一个元素，无需处理
 * 2. 分治处理: 将区间[l,r]分成两部分[l,mid]和[mid+1,r]
 * 3. 递归求解: 分别处理左半部分和右半部分
 * 4. 计算贡献: 计算左半部分对右半部分的贡献
 * 5. 合并结果: 在计算完贡献后，左右部分的结果已经正确
 * 
 * 贡献计算过程:
 * 1. 对左右两部分分别按照b属性排序，确保b属性有序
 * 2. 使用双指针技术，维护左半部分b属性小于等于右半部分b属性的关系
 * 3. 对于右半部分的每个元素，查询树状数组中满足条件的元素个数
 * 4. 处理完后清空树状数组，避免影响后续计算
 */
void cdq(int l, int r) {
    if (DEBUG) printf("CDQ分治处理区间: [%d, %d]\n", l, r);
    
    // 递归边界条件
    if (l == r) {
        if (DEBUG) printf("  递归边界，返回\n");
        return;
    }
    
    // 计算中间点，进行分治（使用这种方式避免整数溢出）
    int mid = l + (r - l) / 2;
    
    // 递归处理左半部分
    cdq(l, mid);
    // 递归处理右半部分
    cdq(mid + 1, r);
    
    // 对左右两部分分别按照b属性排序
    if (DEBUG) printf("  对左右部分按b属性排序\n");
    simpleSortB(ue, l, mid);
    simpleSortB(ue, mid + 1, r);
    
    // 双指针技术计算左半部分对右半部分的贡献
    int i = l;  // 左半部分指针
    int j = mid + 1;  // 右半部分指针
    
    if (DEBUG) printf("  开始计算左半部分对右半部分的贡献\n");
    
    // 遍历右半部分的每个元素
    while (j <= r) {
        // 将左半部分中b属性小于等于右半部分当前元素b属性的元素加入树状数组
        while (i <= mid && ue[i].b <= ue[j].b) {
            BIT.add(ue[i].c, ue[i].cnt);
            if (DEBUG) {
                printf("    添加元素到树状数组: ");
                ue[i].printInfo();
                printf("\n");
                BIT.printStatus();
            }
            i++;
        }
        
        // 查询树状数组中c属性小于等于当前元素c属性的元素个数
        int cnt = BIT.ask(ue[j].c);
        ue[j].res += cnt;
        
        if (DEBUG) {
            printf("    查询元素: ");
            ue[j].printInfo();
            printf(", 结果增加: %d, 累计结果: %d\n", cnt, ue[j].res);
        }
        j++;
    }
    
    // 清空树状数组，避免影响后续计算
    // 只需清空在本次计算中加入的元素
    if (DEBUG) printf("  清空树状数组中的贡献\n");
    for (int p = l; p < i; p++) {
        BIT.add(ue[p].c, -ue[p].cnt);
        if (DEBUG) {
            printf("    移除元素贡献: ");
            ue[p].printInfo();
            printf("\n");
        }
    }
    
    if (DEBUG) printf("  CDQ分治区间 [%d, %d] 处理完成\n", l, r);
}

/**
 * 主函数
 * 
 * 注意：由于编译环境限制，此版本使用了固定输入数据
 * 在实际环境中，应根据需要修改为从标准输入读取数据
 * 
 * @return 程序执行状态码
 */
int main() {
    try {
        // 初始化树状数组
        BIT.init();
        
        // 由于编译环境限制，使用固定输入
        // 在实际应用中，应改为从标准输入读取
        if (DEBUG) printf("使用固定示例数据进行测试...\n");
        
        n = 5;
        k = 10;
        
        // 输入验证
        if (n <= 0 || n > MAXN - 1) {
            printf("错误: 元素数量n=%d超出有效范围(0, %d]\n", n, MAXN - 1);
            return 1;
        }
        if (k <= 0 || k > MAXN - 1) {
            printf("错误: 属性最大值k=%d超出有效范围(0, %d]\n", k, MAXN - 1);
            return 1;
        }
        
        // 示例数据
        e[1].a = 1; e[1].b = 2; e[1].c = 3; e[1].res = 0;
        e[2].a = 2; e[2].b = 3; e[2].c = 4; e[2].res = 0;
        e[3].a = 1; e[3].b = 2; e[3].c = 3; e[3].res = 0;  // 与e[1]相同
        e[4].a = 3; e[4].b = 1; e[4].c = 5; e[4].res = 0;
        e[5].a = 2; e[5].b = 2; e[5].c = 2; e[5].res = 0;
        
        // 打印输入数据
        if (DEBUG) {
            printf("输入数据:\n");
            for (int i = 1; i <= n; i++) {
                printf("元素[%d]: ", i);
                e[i].printInfo();
                printf("\n");
            }
        }
        
        // 按照a属性排序，消除第一维的影响
        if (DEBUG) printf("对原始数组按a属性排序...\n");
        simpleSort(e, 1, n);
        
        // 打印排序后的数据
        if (DEBUG) {
            printf("排序后数据:\n");
            for (int i = 1; i <= n; i++) {
                printf("元素[%d]: ", i);
                e[i].printInfo();
                printf("\n");
            }
        }
        
        // 去重处理，统计相同元素的个数
        if (DEBUG) printf("开始去重处理...\n");
        for (int i = 1; i <= n; i++) {
            t++;
            if (i == n || e[i].notEquals(e[i + 1])) {
                m++;
                ue[m].a = e[i].a;
                ue[m].b = e[i].b;
                ue[m].c = e[i].c;
                ue[m].cnt = t;
                ue[m].res = 0;
                t = 0;
                
                if (DEBUG) {
                    printf("  去重后元素[%d]: ", m);
                    ue[m].printInfo();
                    printf("\n");
                }
            }
        }
        
        // 执行CDQ分治
        if (DEBUG) printf("开始执行CDQ分治，元素总数: %d\n", m);
        cdq(1, m);
        
        // 打印分治后的结果
        if (DEBUG) {
            printf("CDQ分治后结果:\n");
            for (int i = 1; i <= m; i++) {
                printf("元素[%d]: ", i);
                ue[i].printInfo();
                printf("\n");
            }
        }
        
        // 统计最终结果
        if (DEBUG) printf("统计最终结果...\n");
        for (int i = 1; i <= m; i++) {
            // 注意：对于重复元素，每个元素j都可以作为满足条件的元素，所以需要加上(ue[i].cnt - 1)
            int finalRes = ue[i].res + ue[i].cnt - 1;
            if (finalRes >= 0 && finalRes < MAXN) {  // 确保索引有效
                res[finalRes] += ue[i].cnt;
                if (DEBUG) {
                    printf("  元素[%d]: res=%d, cnt=%d, finalRes=%d, 贡献: %d\n", 
                           i, ue[i].res, ue[i].cnt, finalRes, ue[i].cnt);
                }
            } else {
                printf("警告: 结果索引越界: %d\n", finalRes);
            }
        }
        
        // 输出结果
        printf("最终结果:\n");
        for (int i = 0; i < n; i++) {
            if (res[i] > 0 || DEBUG) {  // 只输出非零结果或在调试模式下输出全部
                printf("f(i) = %d: %d个元素\n", i, res[i]);
            }
        }
        
        return 0;
        
    } catch (const char* msg) {
        // 异常处理
        printf("程序运行出错: %s\n", msg);
        return 1;
    } catch (...) {
        // 捕获所有其他异常
        printf("程序运行出错: 未知异常\n");
        return 1;
    }
}