using System;

namespace TypeInterpreter
{
    class Program
    {
        static void Main(string[] args)
        {
            var data1 = 12.3;
            var databytes1 = DataEncode.GetEncodeData(data1.GetTypeCode(), data1);
            var data2 = 'a';
            var databytes2 = DataEncode.GetEncodeData(data2.GetTypeCode(), data2);
            
            Console.WriteLine("sended!");

            var datarecord1 = DataEncode.GetDecodeData(databytes1);
            var datarecord2 = DataEncode.GetDecodeData(databytes2);
            DataParse.TryParse(datarecord1,out int? datareceived1);
            DataParse.TryParse(datarecord2, out char? datareceived2);
            Console.WriteLine($"Received: {datareceived1}");
            Console.WriteLine($"Received: {datareceived2}");
        }
    }
}
