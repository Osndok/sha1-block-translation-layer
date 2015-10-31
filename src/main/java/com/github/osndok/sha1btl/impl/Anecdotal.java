package com.github.osndok.sha1btl.impl;

import java.util.concurrent.TimeUnit;

/**
 * Created by robert on 2015-10-31 14:59.
 */
class Anecdotal
{
	/*
	http://www.storagesearch.com/bitmicro-art3.html (300k)
	https://ef.gy/statistics:ssd-write-endurance (100k)
	http://www.storagesearch.com/ssdmyths-endurance.html (2-3k)
	http://stackoverflow.com/questions/14067491/will-writing-million-times-to-a-file-spoil-my-harddisk (1k; "TLC")
	 */
	public static final int FLASH_WRITE_FATIGUE = 100000;

	/*
	http://www.extremetech.com/computing/205382-ssds-can-lose-data-in-as-little-as-7-days-without-power (7 days to 2 years)
	 */
	public static final long FLASH_CELL_POWERED_LIFE_EXPECTANCY = TimeUnit.DAYS.toMillis(365);

	/*
	http://www.tomshardware.com/forum/255102-32-flash-drive-write-cycles-hard-drive-write-cycles (no limit)
	http://www.extremetech.com/computing/170748-how-long-do-hard-drives-actually-live-for (???)
	 */
	public static final int MAGNETIC_WRITE_FATIGUE = 100 * FLASH_WRITE_FATIGUE;

	/*
	http://www.extremetech.com/computing/170748-how-long-do-hard-drives-actually-live-for (3 years, then hard 'knee'; 80% four years)
	https://www.storagecraft.com/blog/data-storage-lifespan/ (3-to-5 years)
	 */
	public static final long HARD_DISK_LIFE_EXPECTANCY = TimeUnit.DAYS.toMillis(3*365);
}
