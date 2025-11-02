package class165;

// 食物链，Java版
// 动物王国中有三类动物A,B,C，这三类动物的食物链构成了有趣的环形。
// A吃B，B吃C，C吃A。
// 现有N个动物，以1－N编号。
// 每次说话为以下两种之一：
// 1）D X Y，表示X和Y是同类
// 2）D X Y，表示X吃Y
// 判断每句话是否合理，不合理的话为假话
// 1 <= N <= 50000
// 1 <= K <= 100000
// 测试链接 : https://www.luogu.com.cn/problem/P2024
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 相关题目及解析：

// 1. 洛谷 P2024 - 食物链（经典种类并查集）
//    链接：https://www.luogu.com.cn/problem/P2024
//    题目大意：动物有三种关系：同类、捕食、被捕食，根据一些描述判断哪些描述是假的
//    解题思路：使用扩展域并查集，为每个动物创建3个节点分别表示其作为同类、捕食者、被捕食者的关系
//    时间复杂度：O(n + m)，其中m是操作次数，查找操作的均摊复杂度为O(α(n))
//    空间复杂度：O(n)
//    实现细节：
//    - 对于每个动物x，创建三个节点：x（同类）、x+n（捕食者）、x+2n（被捕食者）
//    - 如果x和y是同类，则合并x与y，x+n与y+n，x+2n与y+2n
//    - 如果x吃y，则合并x与y+n，x+n与y+2n，x+2n与y
//    - 每次操作前检查是否存在矛盾

// 2. Codeforces 1444C - Team Building
//    链接：https://codeforces.com/problemset/problem/1444/C
//    题目大意：给定一些人和他们的组别，以及一些矛盾关系，判断两个组是否可以组成一个二分图
//    解题思路：使用扩展域并查集，对于同一组内的矛盾关系，先判断该组是否能构成二分图，对于不同组之间的矛盾关系，使用可撤销并查集判断两个组合并后是否能构成二分图
//    时间复杂度：O((m + k) * log n)
//    空间复杂度：O(n)
//    实现细节：
//    - 对于每个组，使用二分图染色的扩展域并查集
//    - 对于矛盾关系(u, v)，将u与v的敌人合并
//    - 使用可撤销并查集处理不同组之间的合并

// 3. HDU 3038 - How Many Answers Are Wrong
//    链接：http://acm.hdu.edu.cn/showproblem.php?pid=3038
//    题目大意：给出一些区间和的描述，判断哪些描述是错误的
//    解题思路：使用扩展域并查集维护前缀和关系，将区间和转化为前缀和的差
//    时间复杂度：O(n + m)
//    空间复杂度：O(n)
//    实现细节：
//    - 使用带权并查集，权值表示从当前节点到根节点的和
//    - 对于区间[l, r]的和为s，转化为前缀和sum[r] - sum[l-1] = s
//    - 在合并时检查是否存在矛盾

// 4. POJ 1733 - Parity game
//    链接：http://poj.org/problem?id=1733
//    题目大意：判断一个01序列的某些子区间的奇偶性描述是否一致
//    解题思路：使用扩展域并查集维护前缀和的奇偶关系
//    时间复杂度：O(n log n)
//    空间复杂度：O(n)
//    实现细节：
//    - 使用带权并查集，权值表示前缀和的奇偶性
//    - 对于区间[l, r]有偶数个1，转化为sum[r] ≡ sum[l-1] (mod 2)
//    - 对于区间[l, r]有奇数个1，转化为sum[r] ≡ sum[l-1] + 1 (mod 2)

// 5. 洛谷 P1525 - 关押罪犯
//    链接：https://www.luogu.com.cn/problem/P1525
//    题目大意：将罪犯分配到两个监狱，使得冲突值最大的一对罪犯的冲突值最小
//    解题思路：使用扩展域并查集维护罪犯之间的敌对关系
//    时间复杂度：O(n log n)
//    空间复杂度：O(n)
//    实现细节：
//    - 将冲突按强度从大到小排序
//    - 对于每个冲突(u, v)，检查u和v是否已经在同一个集合
//    - 如果是，则这是当前最大的无法避免的冲突
//    - 否则，将u与v的敌人合并，v与u的敌人合并

// 6. LeetCode 721 - 账户合并
//    链接：https://leetcode.cn/problems/accounts-merge/
//    题目大意：将具有相同邮箱的账户合并
//    解题思路：使用扩展域并查集维护邮箱和账户之间的关系
//    时间复杂度：O(n log n)
//    空间复杂度：O(n)
//    实现细节：
//    - 将每个邮箱映射到唯一的ID
//    - 使用并查集合并同一账户的所有邮箱
//    - 最后按账户分组收集所有邮箱

// 思路技巧总结：
// 1. 扩展域并查集适用于处理元素之间的复杂关系，特别是有传递性的关系
// 2. 关键思想是将每个元素的不同状态或关系映射到不同的节点
// 3. 根据问题的关系类型，确定需要扩展的域的数量
// 4. 常见的扩展方式包括：食物链问题（3倍域）、二分图问题（2倍域）、权值问题（带权并查集）
// 5. 使用路径压缩和按秩合并优化时间复杂度

// 工程化考量：
// 1. 内存管理：扩展域会增加空间使用量，需要合理设置数组大小
// 2. 异常处理：需要处理越界访问、无效输入等异常情况
// 3. 性能优化：对于大规模数据，可以使用哈希表代替数组来减少内存占用
// 4. 代码可维护性：使用清晰的变量命名和注释说明各扩展域的含义
// 5. 模块化设计：将并查集操作封装成独立的类，便于复用

// 跨语言实现注意事项：
// 1. Java中需要注意整数溢出问题，特别是在计算扩展域索引时
// 2. C++中可以使用模板来实现通用的扩展域并查集
// 3. Python中字典的访问速度较慢，对于大规模数据可能需要使用数组
// 4. 不同语言的栈深度限制不同，需要注意递归实现的深度问题

/*
 * C++版本实现
#include <iostream>
#include <vector>
using namespace std;

struct ExtendedUnionFind {
    vector<int> parent;  // 存储每个节点的父节点
    vector<int> rank;    // 存储每个节点的秩（树高的上界）
    int n;              // 原始节点数量
    int domain_size;    // 扩展域的总大小（通常为原始数量的3倍）
    
    // 构造函数，初始化扩展域并查集
    ExtendedUnionFind(int size) {
        n = size;
        domain_size = size * 3;  // 每个动物有3个表示：自己、捕食者、被捕食者
        parent.resize(domain_size + 1);
        rank.resize(domain_size + 1, 1);
        
        // 初始化每个节点的父节点为自己
        for (int i = 1; i <= domain_size; i++) {
            parent[i] = i;
        }
    }
    
    // 查找操作，查找x所在集合的根节点，使用路径压缩优化
    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);  // 路径压缩
        }
        return parent[x];
    }
    
    // 合并操作，将x和y所在的集合合并
    void unite(int x, int y) {
        int fx = find(x);
        int fy = find(y);
        
        if (fx != fy) {
            // 按秩合并，将秩小的树合并到秩大的树下
            if (rank[fx] > rank[fy]) {
                swap(fx, fy);
            }
            parent[fx] = fy;
            if (rank[fx] == rank[fy]) {
                rank[fy]++;
            }
        }
    }
    
    // 检查x和y是否在同一个集合
    bool isConnected(int x, int y) {
        return find(x) == find(y);
    }
};

// 解决食物链问题的函数
void solve() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, k;
    cin >> n >> k;
    
    ExtendedUnionFind uf(n);
    int ans = 0;  // 记录假话的数量
    
    for (int i = 0; i < k; i++) {
        int op, x, y;
        cin >> op >> x >> y;
        
        // 检查输入是否合法
        if (x > n || y > n) {
            ans++;
            continue;
        }
        
        if (op == 1) {  // x和y是同类
            // 检查是否存在矛盾
            if (uf.isConnected(x, y + n) || uf.isConnected(x, y + 2 * n)) {
                ans++;
                continue;
            }
            // 合并同类关系
            uf.unite(x, y);               // x和y是同类
            uf.unite(x + n, y + n);       // x的捕食者和y的捕食者是同类
            uf.unite(x + 2 * n, y + 2 * n); // x的被捕食者和y的被捕食者是同类
        } else if (op == 2) {  // x吃y
            // 检查是否存在矛盾
            if (x == y || uf.isConnected(x, y) || uf.isConnected(x, y + n)) {
                ans++;
                continue;
            }
            // 合并捕食关系
            uf.unite(x, y + n);           // x吃y
            uf.unite(x + n, y + 2 * n);   // x的捕食者吃y的被捕食者
            uf.unite(x + 2 * n, y);       // x的被捕食者是y的捕食者
        }
    }
    
    cout << ans << '\n';
}

int main() {
    solve();
    return 0;
}
*/

/*
 * Python版本实现
class ExtendedUnionFind:
    def __init__(self, size):
        self.n = size
        self.domain_size = size * 3  # 每个动物有3个表示：自己、捕食者、被捕食者
        # 初始化父节点数组，每个节点的父节点为自己
        self.parent = list(range(self.domain_size + 1))
        # 初始化秩数组，用于按秩合并
        self.rank = [1] * (self.domain_size + 1)
    
    def find(self, x):
        """查找x所在集合的根节点，使用路径压缩优化"""
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])  # 路径压缩
        return self.parent[x]
    
    def unite(self, x, y):
        """合并x和y所在的集合"""
        fx = self.find(x)
        fy = self.find(y)
        
        if fx != fy:
            # 按秩合并
            if self.rank[fx] > self.rank[fy]:
                fx, fy = fy, fx
            self.parent[fx] = fy
            if self.rank[fx] == self.rank[fy]:
                self.rank[fy] += 1
    
    def is_connected(self, x, y):
        """检查x和y是否在同一个集合"""
        return self.find(x) == self.find(y)

# 解决食物链问题
def solve():
    import sys
    input = sys.stdin.read().split()
    idx = 0
    
    n = int(input[idx])
    idx += 1
    k = int(input[idx])
    idx += 1
    
    uf = ExtendedUnionFind(n)
    ans = 0  # 记录假话的数量
    
    for _ in range(k):
        op = int(input[idx])
        idx += 1
        x = int(input[idx])
        idx += 1
        y = int(input[idx])
        idx += 1
        
        # 检查输入是否合法
        if x > n or y > n:
            ans += 1
            continue
        
        if op == 1:  # x和y是同类
            # 检查是否存在矛盾
            if uf.is_connected(x, y + n) or uf.is_connected(x, y + 2 * n):
                ans += 1
                continue
            # 合并同类关系
            uf.unite(x, y)               # x和y是同类
            uf.unite(x + n, y + n)       # x的捕食者和y的捕食者是同类
            uf.unite(x + 2 * n, y + 2 * n) # x的被捕食者和y的被捕食者是同类
        elif op == 2:  # x吃y
            # 检查是否存在矛盾
            if x == y or uf.is_connected(x, y) or uf.is_connected(x, y + n):
                ans += 1
                continue
            # 合并捕食关系
            uf.unite(x, y + n)           # x吃y
            uf.unite(x + n, y + 2 * n)   # x的捕食者吃y的被捕食者
            uf.unite(x + 2 * n, y)       # x的被捕食者是y的捕食者
    
    print(ans)

if __name__ == "__main__":
    solve()
*/

/*
 * 三种语言实现的对比与分析
 * 
 * 时间复杂度分析：
 * 1. Java版本：使用路径压缩和按秩合并，查找和合并操作的均摊时间复杂度为O(α(n))，
 *    其中α(n)是阿克曼函数的反函数，近似于常数。总体时间复杂度为O(n + kα(n))，
 *    其中k是操作次数。
 * 2. C++版本：与Java版本相同，时间复杂度为O(n + kα(n))。
 * 3. Python版本：理论上与其他两种语言相同，但由于Python的动态特性，常数可能较大。
 * 
 * 空间复杂度分析：
 * 1. Java版本：O(n)，需要存储3n大小的父数组和秩数组。
 * 2. C++版本：与Java版本相同，空间复杂度为O(n)。
 * 3. Python版本：与Java和C++版本相同，空间复杂度为O(n)。
 * 
 * 语言特性差异：
 * 1. Java：使用ArrayList或数组存储，类型安全，内存管理由JVM自动处理。
 * 2. C++：使用vector存储，性能较高，可以更精细地控制内存。
 * 3. Python：代码简洁，但列表的访问速度相对较慢，对于大规模数据可能需要优化。
 * 
 * 工程化考量：
 * 1. 异常处理：在实际应用中，需要添加输入验证和边界检查，确保程序的鲁棒性。
 * 2. 性能优化：对于大规模数据，可以考虑使用更紧凑的数据结构或算法。
 * 3. 内存管理：在C++中需要注意避免内存泄漏，在处理大规模数据时尤为重要。
 * 4. 扩展性：可以根据问题的复杂度扩展到更多类型的关系，例如更多种类的食物链。
 * 
 * 扩展域并查集的应用场景：
 * 1. 处理多类关系的问题，如食物链、种类分类等
 * 2. 判断命题的真假，如程序自动分析问题
 * 3. 处理区间约束问题，如区间和、奇偶性等
 * 4. 解决二分图相关问题
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code05_FoodChain {

	public static int MAXN = 50001;
	
	// 扩展域并查集，每个动物有3个节点：
	// i 表示同类
	// i + n 表示捕食者
	// i + 2 * n 表示被捕食者
	public static int[] father = new int[MAXN * 3];
	public static int[] siz = new int[MAXN * 3];
	
	public static int find(int i) {
		while (i != father[i]) {
			i = father[i];
		}
		return i;
	}
	
	public static void union(int x, int y) {
		int fx = find(x);
		int fy = find(y);
		if (siz[fx] < siz[fy]) {
			int tmp = fx;
			fx = fy;
			fy = tmp;
		}
		father[fy] = fx;
		siz[fx] += siz[fy];
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		in.nextToken();
		int n = (int) in.nval;
		in.nextToken();
		int k = (int) in.nval;
		
		// 初始化并查集
		for (int i = 1; i <= 3 * n; i++) {
			father[i] = i;
			siz[i] = 1;
		}
		
		int falseCount = 0;
		
		for (int i = 1; i <= k; i++) {
			in.nextToken();
			int d = (int) in.nval;
			in.nextToken();
			int x = (int) in.nval;
			in.nextToken();
			int y = (int) in.nval;
			
			// 判断是否越界
			if (x > n || y > n) {
				falseCount++;
				continue;
			}
			
			if (d == 1) {
				// x和y是同类
				// 如果x吃y或y吃x，则为假话
				if (find(x) == find(y + n) || find(x) == find(y + 2 * n) ||
					find(y) == find(x + n) || find(y) == find(x + 2 * n)) {
					falseCount++;
				} else {
					// 合并同类关系
					union(x, y);
					union(x + n, y + n);
					union(x + 2 * n, y + 2 * n);
				}
			} else {
				// x吃y
				// 如果y吃x或x和y是同类，则为假话
				if (find(x) == find(y) || find(x) == find(y + 2 * n) ||
					find(y) == find(x + n)) {
					falseCount++;
				} else {
					// 建立捕食关系
					union(x, y + n);      // x和y的捕食者是同类
					union(x + n, y + 2 * n);  // x的捕食者和y的被捕食者是同类
					union(x + 2 * n, y);      // x的被捕食者和y是同类
				}
			}
		}
		
		out.println(falseCount);
		out.flush();
		out.close();
		br.close();
	}
}