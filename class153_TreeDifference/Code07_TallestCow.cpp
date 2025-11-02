#include <stdio.h>
#include <set>
#include <algorithm>

// Tallest Cow (POJ 3263)
// 有N头牛站成一行，两头牛能够相互看见，当且仅当它们中间的牛身高都比它们矮
// 已知最高的牛是第P头，身高为H，还知道R对关系，每对关系表示两头牛可以相互看见
// 求每头牛的身高最大可能是多少
// 测试链接 : http://poj.org/problem?id=3263

using namespace std;

const int MAXN = 10001;

int n, p, h, r;
int diff[MAXN];
set<long long> seen;

int main() {
    // 读取输入
    scanf("%d%d%d%d", &n, &p, &h, &r);
    
    // 处理每对关系
    for (int i = 0; i < r; i++) {
        int a, b;
        scanf("%d%d", &a, &b);
        
        // 确保a < b，便于处理
        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        }
        
        // 用一个长整型表示一对关系，用于去重
        long long pair = (long long)a * (n + 1) + b;
        if (seen.find(pair) != seen.end()) {
            continue;
        }
        seen.insert(pair);
        
        // 差分操作：在区间(a, b)内的牛身高要减1
        // 即在a+1位置-1，在b位置+1
        diff[a + 1] -= 1;
        diff[b] += 1;
    }
    
    // 通过前缀和计算每头牛的相对身高，然后加上最高身高h得到实际身高
    int height = 0;
    for (int i = 1; i <= n; i++) {
        height += diff[i];
        printf("%d\n", h + height);
    }
    
    return 0;
}