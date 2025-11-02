# LintCode 659. Encode and Decode Strings (字符串编码解码)
# 测试链接 : https://www.lintcode.com/problem/659/

class LintCode659_EncodeDecodeStrings:
    # 编码函数
    def encode(self, strs):
        encoded = []
        
        for s in strs:
            # 格式：长度 + '#' + 字符串
            encoded.append(str(len(s)) + '#' + s)
        
        return ''.join(encoded)
    
    # 解码函数
    def decode(self, s):
        decoded = []
        i = 0
        
        while i < len(s):
            # 找到分隔符'#'
            j = i
            while j < len(s) and s[j] != '#':
                j += 1
            
            # 提取长度
            length = int(s[i:j])
            
            # 提取字符串
            string = s[j + 1:j + 1 + length]
            decoded.append(string)
            
            # 移动到下一个字符串的开始位置
            i = j + 1 + length
        
        return decoded

# 测试用例
def main():
    solution = LintCode659_EncodeDecodeStrings()
    
    # 测试用例1
    strs1 = ["hello", "world"]
    encoded1 = solution.encode(strs1)
    decoded1 = solution.decode(encoded1)
    print(f"输入: {strs1}")
    print(f"编码: {encoded1}")
    print(f"解码: {decoded1}")
    print(f"期望: {strs1}\n")
    
    # 测试用例2
    strs2 = ["", "abc", "def"]
    encoded2 = solution.encode(strs2)
    decoded2 = solution.decode(encoded2)
    print(f"输入: {strs2}")
    print(f"编码: {encoded2}")
    print(f"解码: {decoded2}")
    print(f"期望: {strs2}\n")

if __name__ == "__main__":
    main()