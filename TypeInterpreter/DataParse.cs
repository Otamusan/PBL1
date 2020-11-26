using System;

namespace TypeInterpreter
{
    public static class DataParse
    {
        public static void TryParse(DataRecord dataRecord, out double? dataWithType)
        {
            var parseddata = ContentsParse(dataRecord);
            dataWithType = null;
            if (parseddata is double d)
                dataWithType = d;
        }

        public static void TryParse(DataRecord dataRecord, out int? dataWithType)
        {
            var parseddata = ContentsParse(dataRecord);
            dataWithType = null;
            if (parseddata is int i)
                dataWithType = i;
        }

        public static void TryParse(DataRecord dataRecord, out char? dataWithType)
        {
            var parseddata = ContentsParse(dataRecord);
            dataWithType = null;
            if (parseddata is char c)
                dataWithType = c;
        }

        private static object ContentsParse(DataRecord dataRecord)
        {
            return dataRecord.TypeCode switch
            {
                TypeCode.Int32 => int.Parse(dataRecord.RawData),
                TypeCode.Double => double.Parse(dataRecord.RawData),
                TypeCode.Char => char.Parse(dataRecord.RawData),
                _ => null,
            };
        }
    }
}
