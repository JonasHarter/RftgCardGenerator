package gen.set.definitions;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public enum Phase
{

    SCOUT(1),
    DEVELOP(2),
    SETTLE(3),
    PRODUCE(4),
    SHIP(5);

    private Integer value;

    private Phase(int value)
    {
        this.value = value;
    }

    public Integer getValue()
    {
        return value;
    }

    public static class PhaseAdapter
            extends
            XmlAdapter<Integer, Phase>
    {

        @Override
        public Phase unmarshal(Integer v)
                throws Exception
        {
            for (Phase phase : Phase.values())
            {
                if (v.equals(phase.getValue()))
                    return phase;
            }
            throw new RuntimeException("Failed to find Phase: " + v);
        }

        @Override
        public Integer marshal(Phase v)
                throws Exception
        {
            return v.getValue();
        }

    }
}
