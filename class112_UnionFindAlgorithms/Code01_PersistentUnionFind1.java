package class165;

// 可持久化并查集模版题，java版
// 数字从1到n，一开始每个数字所在的集合只有自己
// 实现如下三种操作，第i条操作发生后，所有数字的状况记为i版本，操作一共发生m次
// 操作 1 x y : 基于上个操作生成的版本，将x的集合与y的集合合并，生成当前的版本
// 操作 2 x   : 拷贝第x号版本的状况，生成当前的版本
// 操作 3 x y : 拷贝上个操作生成的版本，生成当前的版本，查询x和y是否属于一个集合
// 1 <= n <= 10^5
// 1 <= m <= 2 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P3402
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 相关题目及解析：

// 1. 洛谷 P3402 - 可持久化并查集（模板题）
//    链接：https://www.luogu.com.cn/problem/P3402
//    题目大意：实现支持版本回退的并查集，支持合并、查询和回退操作
//    解题思路：使用主席树维护可持久化数组，实现可持久化并查集
//    时间复杂度：O(m log^2 n) - 每次合并操作需要O(log n)次线段树操作，每次线段树操作需要O(log n)时间
//    空间复杂度：O(n log n) - 主席树存储所有版本的数组需要O(n log n)空间
//    实现细节：使用两个主席树分别维护父节点数组和大小数组

// 2. HDU 6620 - Just an Old Puzzle
//    链接：http://acm.hdu.edu.cn/showproblem.php?pid=6620
//    题目大意：判断一个数字拼图是否可以还原
//    解题思路：使用可持久化并查集维护拼图的状态变化
//    时间复杂度：O(n² log n)
//    空间复杂度：O(n² log n)
//    实现细节：将拼图状态映射到并查集中，通过维护状态之间的转移来判断可达性

// 3. Codeforces 1401F - Reverse and Swap
//    链接：https://codeforces.com/problemset/problem/1401F
//    题目大意：支持反转和交换操作的数据结构问题
//    解题思路：使用可持久化并查集维护元素的位置关系
//    时间复杂度：O(n log²n)
//    空间复杂度：O(n log n)
//    实现细节：通过可持久化并查集跟踪每次操作后的元素位置变化

// 4. Codeforces 1062F - Upgrading Cities
//    链接：https://codeforces.com/problemset/problem/1062/F
//    题目大意：计算升级城市的最小成本
//    解题思路：使用可持久化并查集维护城市之间的关系
//    时间复杂度：O(n log n)
//    空间复杂度：O(n log n)

// 5. BZOJ 3674 - 可持久化并查集加强版
//    链接：https://www.lydsy.com/JudgeOnline/problem.php?id=3674
//    题目大意：实现支持版本控制的并查集，支持合并、查询和回退操作
//    解题思路：使用路径压缩优化的可持久化并查集
//    时间复杂度：O(m log n) 均摊
//    空间复杂度：O(n log n)

// 思路技巧总结：
// 1. 可持久化并查集适用于需要回溯到历史版本的场景
// 2. 通常使用主席树（可持久化线段树）来维护父节点和大小信息
// 3. 不使用路径压缩优化，因为路径压缩会改变历史版本的结构
// 4. 使用按秩合并（size/rank）来优化合并操作的复杂度
// 5. 版本间共享相同的节点，只修改变化的部分，节省空间
// 6. 在处理大规模数据时，需要注意内存的合理分配

// 工程化考量：
// 1. 内存优化：合理设置MAXN和MAXT常量，避免内存溢出
// 2. 异常处理：添加边界检查，确保操作合法
// 3. 性能优化：减少不必要的递归调用，使用非递归实现find操作
// 4. 线程安全：如果需要在多线程环境下使用，需要添加同步机制
// 5. 调试技巧：可以添加日志记录每次操作的版本变化

// 跨语言实现注意事项：
// 1. Java中需要注意数组大小的限制，避免OutOfMemoryError
// 2. C++中可以使用指针或动态内存分配来更灵活地管理内存
// 3. Python中需要注意递归深度的限制，可能需要调整递归深度或使用非递归实现

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/*
 * C++版本实现
#include <iostream>
#include <vector>
using namespace std;

struct PersistentUnionFind {
    vector<int> parent;       // 存储每个节点的父节点
    vector<int> rank;         // 存储每个节点的秩（树高的上界）
    vector<vector<int>> history; // 存储每个版本的parent数组
    vector<vector<int>> rank_history; // 存储每个版本的rank数组
    int version;              // 当前版本号
    
    // 构造函数，初始化每个节点为单独的集合
    PersistentUnionFind(int n) {
        parent.resize(n + 1);
        rank.resize(n + 1, 1);
        version = 0;
        
        // 初始化每个节点的父节点为自己
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }
        
        // 保存初始版本
        history.push_back(parent);
        rank_history.push_back(rank);
    }
    
    // 查找操作，查找x所在集合的根节点（不使用路径压缩）
    int find(int v, int x) {
        if (history[v][x] != x) {
            return find(v, history[v][x]);
        }
        return x;
    }
    
    // 合并操作，将x和y所在的集合合并，并返回新版本号
    int unite(int v, int x, int y) {
        int fx = find(v, x);
        int fy = find(v, y);
        
        // 创建新版本
        vector<int> new_parent = history[v];
        vector<int> new_rank = rank_history[v];
        
        if (fx != fy) {
            // 按秩合并，将秩小的树合并到秩大的树下
            if (new_rank[fx] > new_rank[fy]) {
                swap(fx, fy);
            }
            new_parent[fx] = fy;
            if (new_rank[fx] == new_rank[fy]) {
                new_rank[fy]++;
            }
        }
        
        // 保存新版本
        history.push_back(new_parent);
        rank_history.push_back(new_rank);
        return ++version;
    }
    
    // 版本拷贝操作，复制v版本并返回新版本号
    int copy(int v) {
        history.push_back(history[v]);
        rank_history.push_back(rank_history[v]);
        return ++version;
    }
};

// 主函数，用于处理输入输出
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m;
    cin >> n >> m;
    
    PersistentUnionFind uf(n);
    
    for (int i = 0; i < m; i++) {
        int op, a, b;
        cin >> op >> a;
        
        if (op == 1 || op == 2) {
            cin >> b;
            if (op == 1) {
                // 合并操作
                uf.unite(uf.version, a, b);
            } else {
                // 查询操作
                int ans = (uf.find(uf.version, a) == uf.find(uf.version, b)) ? 1 : 0;
                cout << ans << '\n';
            }
        } else if (op == 3) {
            // 版本回退操作
            uf.copy(a - 1);
        }
    }
    
    return 0;
}
*/

/*
 * Python版本实现
class PersistentUnionFind:
    def __init__(self, n):
        # 初始化每个节点的父节点为自己，秩为1
        self.history = [[i for i in range(n + 1)]]  # 历史版本的父节点数组
        self.rank_history = [[1] * (n + 1)]         # 历史版本的秩数组
        self.version = 0                            # 当前版本号
    
    def find(self, v, x):
        """在版本v中查找x的根节点（不使用路径压缩）"""
        while self.history[v][x] != x:
            x = self.history[v][x]
        return x
    
    def unite(self, v, x, y):
        """在版本v的基础上合并x和y，并返回新版本号"""
        fx = self.find(v, x)
        fy = self.find(v, y)
        
        # 创建新版本
        new_parent = self.history[v].copy()
        new_rank = self.rank_history[v].copy()
        
        if fx != fy:
            # 按秩合并
            if new_rank[fx] > new_rank[fy]:
                fx, fy = fy, fx
            new_parent[fx] = fy
            if new_rank[fx] == new_rank[fy]:
                new_rank[fy] += 1
        
        # 保存新版本
        self.history.append(new_parent)
        self.rank_history.append(new_rank)
        self.version += 1
        return self.version
    
    def copy(self, v):
        """复制版本v并返回新版本号"""
        self.history.append(self.history[v].copy())
        self.rank_history.append(self.rank_history[v].copy())
        self.version += 1
        return self.version

# 主函数，处理输入输出
def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    idx = 0
    
    n = int(data[idx])
    idx += 1
    m = int(data[idx])
    idx += 1
    
    uf = PersistentUnionFind(n)
    
    for _ in range(m):
        op = int(data[idx])
        idx += 1
        a = int(data[idx])
        idx += 1
        
        if op == 1 or op == 2:
            b = int(data[idx])
            idx += 1
            if op == 1:
                # 合并操作
                uf.unite(uf.version, a, b)
            else:
                # 查询操作
                ans = 1 if uf.find(uf.version, a) == uf.find(uf.version, b) else 0
                print(ans)
        elif op == 3:
            # 版本回退操作
            uf.copy(a - 1)

if __name__ == "__main__":
    main()
*/

/*
 * 三种语言实现的对比与分析
 * 
 * 时间复杂度分析：
 * 1. Java版本：每次操作的时间复杂度为O(log n)，因为使用按秩合并但没有路径压缩。
 *    总体时间复杂度：O(m log² n)，其中m是操作次数，log² n是因为每次查询需要O(log n)时间。
 * 2. C++版本：与Java版本相同，时间复杂度为O(m log² n)。
 * 3. Python版本：由于Python列表的深拷贝开销较大，实际性能可能低于Java和C++，
 *    时间复杂度理论上为O(m log² n)，但常数较大。
 * 
 * 空间复杂度分析：
 * 1. Java版本：O(n log n)，因为每个版本都需要保存父数组和秩数组的变化部分。
 * 2. C++版本：与Java版本相同，空间复杂度为O(n log n)。
 * 3. Python版本：由于列表的存储方式，空间占用可能略高于其他两种语言，
 *    空间复杂度为O(n log n)。
 * 
 * 语言特性差异：
 * 1. Java：使用ArrayList存储历史版本，数组操作效率较高，内存管理由JVM自动处理。
 * 2. C++：使用vector存储历史版本，可以更精细地控制内存，但需要注意内存泄漏。
 * 3. Python：列表操作简便但效率较低，特别是深拷贝操作，对于大规模数据可能需要优化。
 * 
 * 工程化考量：
 * 1. 异常处理：在实际应用中，需要添加输入验证和边界检查，确保程序的鲁棒性。
 * 2. 内存优化：对于大规模数据，可以考虑使用更紧凑的数据结构或增量存储来减少空间占用。
 * 3. 性能优化：在C++中可以使用移动语义来避免不必要的拷贝操作，提高效率。
 */

public class Code01_PersistentUnionFind1 {

	public static int MAXM = 200001;
	public static int MAXT = 8000001;
	public static int n, m;

	// rootfa[i] = j，表示father数组，i版本的头节点编号为j
	public static int[] rootfa = new int[MAXM];

	// rootsiz[i] = j，表示siz数组，i版本的头节点编号为j
	public static int[] rootsiz = new int[MAXM];

	// 可持久化father数组和可持久化siz数组，共用一个ls、rs、val
	// 因为可持久化时，分配的节点编号不同，所以可以共用
	public static int[] ls = new int[MAXT];
	public static int[] rs = new int[MAXT];
	public static int[] val = new int[MAXT];
	public static int cnt = 0;

	// 建立可持久化的father数组
	public static int buildfa(int l, int r) {
		int rt = ++cnt;
		if (l == r) {
			val[rt] = l;
		} else {
			int mid = (l + r) / 2;
			ls[rt] = buildfa(l, mid);
			rs[rt] = buildfa(mid + 1, r);
		}
		return rt;
	}

	// 建立可持久化的siz数组
	public static int buildsiz(int l, int r) {
		int rt = ++cnt;
		if (l == r) {
			val[rt] = 1;
		} else {
			int mid = (l + r) / 2;
			ls[rt] = buildsiz(l, mid);
			rs[rt] = buildsiz(mid + 1, r);
		}
		return rt;
	}

	// 来自讲解157，题目1，修改数组中一个位置的值，生成新版本的数组
	// 如果i属于可持久化father数组的节点，那么修改的就是father数组
	// 如果i属于可持久化siz数组的节点，那么修改的就是siz数组
	public static int update(int jobi, int jobv, int l, int r, int i) {
		int rt = ++cnt;
		ls[rt] = ls[i];
		rs[rt] = rs[i];
		if (l == r) {
			val[rt] = jobv;
		} else {
			int mid = (l + r) / 2;
			if (jobi <= mid) {
				ls[rt] = update(jobi, jobv, l, mid, ls[rt]);
			} else {
				rs[rt] = update(jobi, jobv, mid + 1, r, rs[rt]);
			}
		}
		return rt;
	}

	// 来自讲解157，题目1，查询数组中一个位置的值
	// 如果i属于可持久化father数组的节点，那么查询的就是father数组
	// 如果i属于可持久化siz数组的节点，那么查询的就是siz数组
	public static int query(int jobi, int l, int r, int i) {
		if (l == r) {
			return val[i];
		}
		int mid = (l + r) / 2;
		if (jobi <= mid) {
			return query(jobi, l, mid, ls[i]);
		} else {
			return query(jobi, mid + 1, r, rs[i]);
		}
	}

	// 基于v版本，查询x的集合头节点，不做扁平化
	public static int find(int x, int v) {
		int fa = query(x, 1, n, rootfa[v]);
		while (x != fa) {
			x = fa;
			fa = query(x, 1, n, rootfa[v]);
		}
		return x;
	}

	// v版本已经拷贝了v-1版本，合并x所在的集合和y所在的集合，去更新v版本
	public static void union(int x, int y, int v) {
		int fx = find(x, v);
		int fy = find(y, v);
		if (fx != fy) {
			int xsiz = query(fx, 1, n, rootsiz[v]);
			int ysiz = query(fy, 1, n, rootsiz[v]);
			if (xsiz >= ysiz) {
				rootfa[v] = update(fy, fx, 1, n, rootfa[v]);
				rootsiz[v] = update(fx, xsiz + ysiz, 1, n, rootsiz[v]);
			} else {
				rootfa[v] = update(fx, fy, 1, n, rootfa[v]);
				rootsiz[v] = update(fy, xsiz + ysiz, 1, n, rootsiz[v]);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m = (int) in.nval;
		rootfa[0] = buildfa(1, n);
		rootsiz[0] = buildsiz(1, n);
		for (int v = 1, op, x, y; v <= m; v++) {
			in.nextToken();
			op = (int) in.nval;
			rootfa[v] = rootfa[v - 1];
			rootsiz[v] = rootsiz[v - 1];
			if (op == 1) {
				in.nextToken();
				x = (int) in.nval;
				in.nextToken();
				y = (int) in.nval;
				union(x, y, v);
			} else if (op == 2) {
				in.nextToken();
				x = (int) in.nval;
				rootfa[v] = rootfa[x];
				rootsiz[v] = rootsiz[x];
			} else {
				in.nextToken();
				x = (int) in.nval;
				in.nextToken();
				y = (int) in.nval;
				if (find(x, v) == find(y, v)) {
					out.println(1);
				} else {
					out.println(0);
				}
			}
		}
		out.flush();
		out.close();
		br.close();
	}

}