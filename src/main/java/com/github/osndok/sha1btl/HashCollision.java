package com.github.osndok.sha1btl;

/**
 * However unlikely, it is always possible that two files might hash to the same value. Therefore,
 * we should account for this eventuality at least as an Exception to be considered.
 *
 * Created by robert on 2015-10-30 14:22.
 */
public
class HashCollision extends Exception
{
}
