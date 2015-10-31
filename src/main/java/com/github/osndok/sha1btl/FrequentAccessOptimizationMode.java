package com.github.osndok.sha1btl;

/**
 * Since we intend to include logic to "migrate" blocks (particularly to support MTD block erasures),
 * it makes sense to add just a touch of flexibility into the algorithm... Where do you want the data
 * to move? As it may not always be best to push it towards the center.
 *
 * Created by robert on 2015-10-31 14:41.
 */
public
enum FrequentAccessOptimizationMode
{
	/**
	 * For non-solid-state media (like hard disks, CDs, and Blu-ray disks... but probably even tape-drives,
	 * warehouse robots and other fun and cooky experiments), it is often the case that having frequently
	 * accessed blocks stored (or more literally read) closer towards the *center* of the available address
	 * space (as opposed to the near-zero and near-max edges).
	 */
	CENTRALIZED,

	/**
	 * Perhaps they use linear tapes, that must always be rewound? Wouldn't it be swell to have the frequently
	 * accessed data near the front? Or else, maybe they have an SSD and are thinking about, eventually, migrating
	 * to a smaller disk? Wouldn't it be nice if all the blocks were already in the smaller region?
	 */
	NEAR_FRONT,

	/**
	 * If we are going to have NEAR_FRONT, I guess we might as well have a NEAR_BACK, for completeness. Perhaps
	 * we are actually hiding blocks in the freespace of another partition,
	 */
	NEAR_BACK,
	DIFFUSE
}
