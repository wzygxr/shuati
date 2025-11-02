package class153;

/**
 * Splay树实现 - C++版本（注释形式）
 * 【题目来源】洛谷 P3369
 * 【题目链接】https://www.luogu.com.cn/problem/P3369
 * 【算法说明】
 * 此文件包含Splay树的C++实现代码，以注释形式保存在Java文件中
 * 实现了普通平衡树的六种基本操作，逻辑与Java版本完全一致
 * 【实现特点】
 * - 使用数组模拟节点结构，避免动态内存分配开销
 * - 不使用词频压缩，每个重复元素作为单独节点存储
 * - 包含完整的C++头文件和命名空间声明
 */

/**
 * C++版本的Splay树实现详解：
 * 1. 时间复杂度：所有操作均摊O(log n)，单次最坏O(n)
 * 2. 空间复杂度：O(n)
 * 3. 与Java版本的区别：
 *    - 使用数组而非ArrayList存储节点信息
 *    - IO优化使用ios::sync_with_stdio(false)
 *    - 变量命名更符合C++风格（如fa、ls、rs、siz）
 * 4. 核心算法完全一致：旋转操作、伸展操作、各类查询和修改操作
 */

//#include <iostream>
//#include <vector>
//#include <algorithm>
//#include <climits>
//
//using namespace std;
//
//const int MAXN = 100001;  // 【空间配置】预分配节点数量，对应操作数上限
//
//int head = 0;  // 【树结构标识】根节点索引
//int cnt = 0;   // 【树结构标识】节点计数器
//int key[MAXN]; // 【节点属性】节点存储的值
//int fa[MAXN];  // 【节点属性】父节点索引
//int ls[MAXN];  // 【节点属性】左子节点索引 (left son)
//int rs[MAXN];  // 【节点属性】右子节点索引 (right son)
//int siz[MAXN]; // 【节点属性】子树大小
//
///**
// * 【自底向上维护】更新节点子树大小
// * 时间复杂度: O(1)
// * @param i 需要更新的节点索引
// */
//void up(int i) {
//    siz[i] = siz[ls[i]] + siz[rs[i]] + 1;
//}
//
///**
// * 【方向判断】确定节点i是其父节点的左子还是右子
// * 时间复杂度: O(1)
// * @param i 需要判断的节点索引
// * @return 1表示右子节点，0表示左子节点
// */
//int lr(int i) {
//    return rs[fa[i]] == i ? 1 : 0;
//}
//
///**
// * 【核心旋转操作】将节点i旋转至其父节点的位置
// * 时间复杂度: O(1)
// * @param i 需要旋转的节点索引
// */
//void rotate(int i) {
//    int f = fa[i];     // 父节点索引
//    int g = fa[f];     // 祖父节点索引
//    int soni = lr(i);  // 当前节点是父节点的左子还是右子
//    int sonf = lr(f);  // 父节点是祖父节点的左子还是右子
//    
//    // 根据当前节点是左子还是右子执行不同的旋转操作
//    if (soni == 1) {   // 右子节点，执行右旋
//        rs[f] = ls[i];
//        if (rs[f] != 0) {
//            fa[rs[f]] = f;
//        }
//        ls[i] = f;
//    } else {           // 左子节点，执行左旋
//        ls[f] = rs[i];
//        if (ls[f] != 0) {
//            fa[ls[f]] = f;
//        }
//        rs[i] = f;
//    }
//    
//    // 更新祖父节点的子节点指针
//    if (g != 0) {
//        if (sonf == 1) {
//            rs[g] = i;
//        } else {
//            ls[g] = i;
//        }
//    }
//    
//    // 更新父指针
//    fa[f] = i;
//    fa[i] = g;
//    
//    // 更新节点信息，先更新被旋转的父节点，再更新当前节点
//    up(f);
//    up(i);
//}
//
///**
// * 【核心伸展操作】将节点i旋转到goal的子节点位置
// * 如果goal为0，则将i旋转到根节点
// * 时间复杂度: 均摊O(log n)
// * @param i 需要旋转的节点索引
// * @param goal 目标父节点索引
// */
//void splay(int i, int goal) {
//    int f = fa[i], g = fa[f];
//    
//    // 当当前节点的父节点不是目标节点时，继续旋转
//    while (f != goal) {
//        // 根据Zig-Zig和Zig-Zag情况选择不同的旋转顺序
//        if (g != goal) {
//            if (lr(i) == lr(f)) {  // Zig-Zig情况
//                rotate(f);
//            } else {               // Zig-Zag情况
//                rotate(i);
//            }
//        }
//        rotate(i);
//        
//        // 更新父节点和祖父节点
//        f = fa[i];
//        g = fa[f];
//    }
//    
//    // 如果旋转到根节点，更新根节点指针
//    if (goal == 0) {
//        head = i;
//    }
//}
//
///**
// * 【查找操作】在整棵树中找到中序遍历排名为rank的节点
// * 【特殊注意】此方法不进行提根操作，仅作为内部方法使用
// * 这是因为remove方法在调用此方法时，要求节点不被提根
// * 时间复杂度: O(log n)
// * @param rank 目标排名
// * @return 对应排名的节点索引
// */
//int find(int rank) {
//    int i = head;
//    while (i != 0) {
//        if (siz[ls[i]] + 1 == rank) {
//            return i;
//        } else if (siz[ls[i]] >= rank) {
//            i = ls[i];
//        } else {
//            rank -= siz[ls[i]] + 1;
//            i = rs[i];
//        }
//    }
//    return 0; // 未找到对应排名的节点
//}
//
///**
// * 【插入操作】向Splay树中插入一个新元素
// * 插入后将新节点提至根，以优化后续访问
// * 时间复杂度: 均摊O(log n)
// * 空间复杂度: O(1)
// * @param num 需要插入的元素值
// */
//void add(int num) {
//    // 创建新节点
//    key[++cnt] = num;
//    siz[cnt] = 1;
//    
//    // 【空树处理】如果树为空，直接设置为根节点
//    if (head == 0) {
//        head = cnt;
//    } else {
//        // 【查找插入位置】根据BST性质找到合适的插入位置
//        int f = 0, i = head, son = 0;
//        while (i != 0) {
//            f = i;
//            if (key[i] <= num) {
//                son = 1;
//                i = rs[i];
//            } else {
//                son = 0;
//                i = ls[i];
//            }
//        }
//        
//        // 插入节点到找到的位置
//        if (son == 1) {
//            rs[f] = cnt;
//        } else {
//            ls[f] = cnt;
//        }
//        fa[cnt] = f;
//        
//        // 【重要优化】将刚插入的节点旋转至根，优化后续访问
//        splay(cnt, 0);
//    }
//}
//
///**
// * 【查询排名】查询元素num在树中的排名
// * 排名定义为：比num小的元素个数 + 1
// * 时间复杂度: 均摊O(log n)
// * @param num 要查询的元素值
// * @return num的排名
// */
//int getRank(int num) {
//    int i = head, last = head;
//    int ans = 0;
//    
//    // 【遍历查找】同时计算比num小的元素数量
//    while (i != 0) {
//        last = i;
//        if (key[i] >= num) {
//            i = ls[i];
//        } else {
//            // 累加左子树节点数和当前节点
//            ans += siz[ls[i]] + 1;
//            i = rs[i];
//        }
//    }
//    
//    // 【重要优化】将最后访问的节点旋转至根，优化后续访问
//    splay(last, 0);
//    return ans + 1; // 排名 = 比num小的元素数 + 1
//}
//
///**
// * 【查询第k大元素】查询排名为x的元素值
// * 时间复杂度: 均摊O(log n)
// * @param x 目标排名
// * @return 对应排名的元素值
// */
//int index(int x) {
//    int i = find(x);
//    // 【重要优化】将找到的节点旋转至根，优化后续访问
//    splay(i, 0);
//    return key[i];
//}
//
///**
// * 【查询前驱】查询小于num的最大元素
// * 不存在时返回INT_MIN
// * 时间复杂度: 均摊O(log n)
// * @param num 目标元素
// * @return 前驱元素值
// */
//int pre(int num) {
//    int i = head, last = head;
//    int ans = INT_MIN;
//    
//    // 【遍历查找】寻找小于num的最大元素
//    while (i != 0) {
//        last = i;
//        if (key[i] >= num) {
//            i = ls[i];
//        } else {
//            // 更新可能的前驱元素
//            ans = max(ans, key[i]);
//            i = rs[i];
//        }
//    }
//    
//    // 【重要优化】将最后访问的节点旋转至根，优化后续访问
//    splay(last, 0);
//    return ans;
//}
//
///**
// * 【查询后继】查询大于num的最小元素
// * 不存在时返回INT_MAX
// * 时间复杂度: 均摊O(log n)
// * @param num 目标元素
// * @return 后继元素值
// */
//int post(int num) {
//    int i = head, last = head;
//    int ans = INT_MAX;
//    
//    // 【遍历查找】寻找大于num的最小元素
//    while (i != 0) {
//        last = i;
//        if (key[i] <= num) {
//            i = rs[i];
//        } else {
//            // 更新可能的后继元素
//            ans = min(ans, key[i]);
//            i = ls[i];
//        }
//    }
//    
//    // 【重要优化】将最后访问的节点旋转至根，优化后续访问
//    splay(last, 0);
//    return ans;
//}
//
///**
// * 【删除操作】从树中删除一个等于num的元素
// * 如果有多个，只删除一个
// * 时间复杂度: 均摊O(log n)
// * @param num 需要删除的元素值
// */
//void remove(int num) {
//    // 【存在性检查】如果num不存在，直接返回
//    int kth = getRank(num);
//    if (kth != getRank(num + 1)) {
//        // 找到第一个等于num的节点并旋转至根
//        int i = find(kth);
//        splay(i, 0);
//        
//        // 【删除策略】根据子树情况选择不同的删除方式
//        if (ls[i] == 0) {
//            // 没有左子树，直接用右子树替换
//            head = rs[i];
//        } else if (rs[i] == 0) {
//            // 没有右子树，直接用左子树替换
//            head = ls[i];
//        } else {
//            // 同时存在左右子树
//            // 找到中序遍历的后继节点（右子树的最小节点）
//            int j = find(kth + 1);
//            // 将后继节点旋转至当前节点的右子节点
//            splay(j, i);
//            // 将左子树挂载到后继节点下
//            ls[j] = ls[i];
//            fa[ls[j]] = j;
//            // 更新后继节点的大小信息
//            up(j);
//            // 将后继节点设为新的根
//            head = j;
//        }
//        // 确保新根的父指针为空（如果存在）
//        if (head != 0) {
//            fa[head] = 0;
//        }
//    }
//}
//
///**
// * 【主函数】处理输入输出和操作调用
// * 【输入输出优化】使用ios::sync_with_stdio(false)提高读取效率
// * @return 程序执行状态码
// */
//int main() {
//    // 【IO优化】关闭同步以提高速度
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    
//    int n;
//    cin >> n; // 读取操作次数
//    
//    // 处理每个操作
//    for (int i = 0, op, x; i < n; i++) {
//        cin >> op >> x; // 读取操作类型和参数
//        
//        // 根据操作类型执行相应操作
//        if (op == 1) {
//            // 操作1: 插入元素x
//            add(x);
//        } else if (op == 2) {
//            // 操作2: 删除元素x
//            remove(x);
//        } else if (op == 3) {
//            // 操作3: 查询x的排名
//            cout << getRank(x) << endl;
//        } else if (op == 4) {
//            // 操作4: 查询排名为x的元素
//            cout << index(x) << endl;
//        } else if (op == 5) {
//            // 操作5: 查询x的前驱
//            cout << pre(x) << endl;
//        } else {
//            // 操作6: 查询x的后继
//            cout << post(x) << endl;
//        }
//    }
//    return 0;
//}