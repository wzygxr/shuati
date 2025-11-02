"""
LeetCode 535. TinyURL 的加密与解密
题目链接：https://leetcode.com/problems/encode-and-decode-tinyurl/

题目描述：
TinyURL是一种URL简化服务。比如当你输入一个URL https://leetcode.com/problems/design-tinyurl时，
它将返回一个简化的URL http://tinyurl.com/4e9iAk。
要求设计一个TinyURL系统，包含加密和解密两个功能：
- 加密：将长URL转换为短URL
- 解密：将短URL转换回长URL

算法思路：
1. 使用哈希表存储长URL到短URL的映射关系
2. 使用自增ID或哈希值生成唯一的短URL标识符
3. 将标识符编码为短字符串（如62进制）
4. 使用前缀"http://tinyurl.com/"构成完整短URL

时间复杂度：
- encode: O(1) 平均情况
- decode: O(1) 平均情况

空间复杂度：O(n)，其中n是存储的URL数量
"""

import threading
import random
from collections import defaultdict

class Codec:
    """TinyURL系统实现类"""
    
    # 字符集：62个字符（数字0-9，小写字母a-z，大写字母A-Z）
    CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    BASE = len(CHARSET)  # 62进制
    PREFIX = "http://tinyurl.com/"
    
    def __init__(self):
        """初始化TinyURL系统"""
        self.short_to_long_map = {}  # 存储短URL到长URL的映射
        self.long_to_short_map = {}  # 存储长URL到短URL的映射（避免重复生成）
        self.id_generator = 0        # 自增ID生成器
        self.lock = threading.Lock() # 线程锁
    
    def encode(self, longUrl: str) -> str:
        """
        将长URL编码为短URL
        
        Args:
            longUrl: 长URL
            
        Returns:
            短URL
        """
        with self.lock:
            # 检查是否已经为该长URL生成过短URL
            if longUrl in self.long_to_short_map:
                return self.long_to_short_map[longUrl]
            
            # 生成新的唯一ID
            self.id_generator += 1
            id = self.id_generator
            
            # 将ID转换为62进制字符串
            short_key = self._id_to_short_key(id)
            
            # 构造短URL
            short_url = self.PREFIX + short_key
            
            # 存储映射关系
            self.short_to_long_map[short_key] = longUrl
            self.long_to_short_map[longUrl] = short_url
            
            return short_url
    
    def decode(self, shortUrl: str) -> str:
        """
        将短URL解码为长URL
        
        Args:
            shortUrl: 短URL
            
        Returns:
            长URL
        """
        with self.lock:
            # 提取短键
            short_key = shortUrl[len(self.PREFIX):]
            
            # 查找对应的长URL
            return self.short_to_long_map.get(short_key, "")
    
    def _id_to_short_key(self, id: int) -> str:
        """
        将ID转换为62进制字符串
        
        Args:
            id: ID
            
        Returns:
            62进制字符串
        """
        if id == 0:
            return self.CHARSET[0]
        
        short_key = []
        while id > 0:
            short_key.append(self.CHARSET[id % self.BASE])
            id //= self.BASE
        return ''.join(reversed(short_key))
    
    def _short_key_to_id(self, short_key: str) -> int:
        """
        将62进制字符串转换为ID
        
        Args:
            short_key: 62进制字符串
            
        Returns:
            ID
        """
        id = 0
        for char in short_key:
            id = id * self.BASE + self.CHARSET.index(char)
        return id
    
    def get_statistics(self) -> dict:
        """
        获取系统统计信息
        
        Returns:
            统计信息
        """
        with self.lock:
            return {
                "url_count": len(self.short_to_long_map),
                "next_id": self.id_generator
            }


class CodecOptimized:
    """优化版本：使用哈希值作为ID"""
    
    CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    BASE = len(CHARSET)
    PREFIX = "http://tinyurl.com/"
    
    def __init__(self):
        """初始化TinyURL系统"""
        self.short_to_long_map = {}
        self.long_to_short_map = {}
        self.lock = threading.Lock()
    
    def encode(self, longUrl: str) -> str:
        """
        将长URL编码为短URL
        
        Args:
            longUrl: 长URL
            
        Returns:
            短URL
        """
        with self.lock:
            if longUrl in self.long_to_short_map:
                return self.long_to_short_map[longUrl]
            
            # 使用URL的哈希值作为ID
            hash_val = hash(longUrl)
            short_key = self._hash_to_short_key(hash_val)
            
            # 处理哈希冲突
            while short_key in self.short_to_long_map and \
                  self.short_to_long_map[short_key] != longUrl:
                # 如果发生冲突，添加随机字符
                short_key += random.choice(self.CHARSET)
            
            short_url = self.PREFIX + short_key
            self.short_to_long_map[short_key] = longUrl
            self.long_to_short_map[longUrl] = short_url
            
            return short_url
    
    def decode(self, shortUrl: str) -> str:
        """
        将短URL解码为长URL
        
        Args:
            shortUrl: 短URL
            
        Returns:
            长URL
        """
        with self.lock:
            short_key = shortUrl[len(self.PREFIX):]
            return self.short_to_long_map.get(short_key, "")
    
    def _hash_to_short_key(self, hash_val: int) -> str:
        """
        将哈希值转换为短键
        
        Args:
            hash_val: 哈希值
            
        Returns:
            短键
        """
        # 取绝对值避免负数
        abs_hash = abs(hash_val)
        
        if abs_hash == 0:
            short_key = [self.CHARSET[0]]
        else:
            short_key = []
            while abs_hash > 0:
                short_key.append(self.CHARSET[abs_hash % self.BASE])
                abs_hash //= self.BASE
        
        # 确保至少有6个字符
        while len(short_key) < 6:
            short_key.append(random.choice(self.CHARSET))
        
        return ''.join(reversed(short_key))


def test_basic_version():
    """基础版本测试"""
    print("--- 基础版本测试 ---")
    codec = Codec()
    
    # 测试基本功能
    url1 = "https://leetcode.com/problems/design-tinyurl"
    short_url1 = codec.encode(url1)
    decoded_url1 = codec.decode(short_url1)
    
    print(f"原始URL: {url1}")
    print(f"短URL: {short_url1}")
    print(f"解码URL: {decoded_url1}")
    print(f"编码解码一致性: {url1 == decoded_url1}")
    
    # 测试重复URL
    short_url1_again = codec.encode(url1)
    print(f"重复编码一致性: {short_url1 == short_url1_again}")
    
    # 测试不同URL
    url2 = "https://www.google.com"
    short_url2 = codec.encode(url2)
    decoded_url2 = codec.decode(short_url2)
    
    print(f"URL2原始: {url2}")
    print(f"URL2短URL: {short_url2}")
    print(f"URL2解码: {decoded_url2}")
    print(f"URL2一致性: {url2 == decoded_url2}")
    
    # 统计信息
    print(f"系统统计: {codec.get_statistics()}")
    print()


def test_optimized_version():
    """优化版本测试"""
    print("--- 优化版本测试 ---")
    codec = CodecOptimized()
    
    # 测试基本功能
    url1 = "https://leetcode.com/problems/design-tinyurl"
    short_url1 = codec.encode(url1)
    decoded_url1 = codec.decode(short_url1)
    
    print(f"原始URL: {url1}")
    print(f"短URL: {short_url1}")
    print(f"解码URL: {decoded_url1}")
    print(f"编码解码一致性: {url1 == decoded_url1}")
    
    # 测试重复URL
    short_url1_again = codec.encode(url1)
    print(f"重复编码一致性: {short_url1 == short_url1_again}")
    print()


def performance_test():
    """性能测试"""
    print("--- 性能测试 ---")
    codec = Codec()
    
    # 生成测试URL
    test_urls = [f"https://example.com/page{i}.html" for i in range(10000)]
    
    # 测试编码性能
    import time
    start_time = time.time()
    short_urls = [codec.encode(url) for url in test_urls]
    encode_time = time.time() - start_time
    
    # 测试解码性能
    start_time = time.time()
    for short_url in short_urls:
        codec.decode(short_url)
    decode_time = time.time() - start_time
    
    print(f"编码10000个URL耗时: {encode_time*1000:.2f}ms")
    print(f"解码10000个URL耗时: {decode_time*1000:.2f}ms")
    print(f"平均每URL编码耗时: {encode_time*1000/10000:.4f}ms")
    print(f"平均每URL解码耗时: {decode_time*1000/10000:.4f}ms")
    print(f"系统统计: {codec.get_statistics()}")
    print()


def edge_case_test():
    """边界情况测试"""
    print("--- 边界情况测试 ---")
    codec = Codec()
    
    # 测试空URL
    empty_url = ""
    short_empty = codec.encode(empty_url)
    decoded_empty = codec.decode(short_empty)
    print(f"空URL编码: {short_empty}")
    print(f"空URL解码: '{decoded_empty}'")
    
    # 测试无效短URL
    invalid_short = codec.decode("http://tinyurl.com/invalid")
    print(f"无效短URL解码: '{invalid_short}'")
    
    # 测试超长URL
    long_url = "http://example.com/" + "/".join([f"path{i}" for i in range(1000)])
    short_long = codec.encode(long_url)
    decoded_long = codec.decode(short_long)
    print(f"超长URL一致性: {long_url == decoded_long}")
    
    print()


if __name__ == "__main__":
    print("=== 测试 LeetCode 535. TinyURL 的加密与解密 ===")
    
    # 基础版本测试
    test_basic_version()
    
    # 优化版本测试
    test_optimized_version()
    
    # 性能测试
    performance_test()
    
    # 边界情况测试
    edge_case_test()