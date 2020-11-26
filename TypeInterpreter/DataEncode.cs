using System;
using System.Text;
using System.Text.RegularExpressions;

namespace TypeInterpreter
{
    public static class DataEncode
    {
        public static byte[] GetEncodeData(TypeCode typeCode, object obj)
        {
            var processed = $"{typeCode}/{obj}";
            return Encoding.ASCII.GetBytes(processed);
        }
        
        public static DataRecord GetDecodeData(byte[] bytes)
        {
            var decoded = Encoding.ASCII.GetString(bytes);
            var match = Regex.Match(decoded, @"(.*?)/(.*)");

            _ = Enum.TryParse(match.Groups[1].Value, out TypeCode typecode);
            var rawdata = match.Groups[2].Value;

            return new DataRecord
            {
                TypeCode = typecode,
                RawData = rawdata,
            };
        }
    }
}
