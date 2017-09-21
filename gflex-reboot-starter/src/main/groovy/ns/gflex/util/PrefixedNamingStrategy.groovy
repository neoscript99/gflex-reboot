package ns.gflex.util

import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * Functions
 * @since Dec 22, 2010
 * @author wangchu
 */
class PrefixedNamingStrategy extends ImprovedNamingStrategy
{

	@Override
	public String classToTableName(String className)
	{
		return "sys_${super.classToTableName(className)}";
	}

}
