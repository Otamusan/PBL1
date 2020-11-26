using System;

namespace TypeInterpreter
{
    public record DataRecord
    {
        public TypeCode TypeCode { get; init; }
        public string RawData { get; init; }
    }
}
