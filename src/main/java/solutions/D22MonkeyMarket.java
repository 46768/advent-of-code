package solutions;

import java.util.*;

import dayBase.DayBase;
import input.InputData;
import logger.Logger;

public class D22MonkeyMarket extends DayBase<List<Long>> {
	public D22MonkeyMarket(InputData dat) {
		super(dat);
	}

	protected List<Long> parseInput(ArrayList<String> dat) {
		List<Long> initSecret = new ArrayList<>();

		for (String line : dat) {
			initSecret.add(Long.parseUnsignedLong(line));
		}

		return initSecret;
	}

	private long calculateNextSecret(long secret) {
		final long PRUNE_CONSTANT = 16777216;
		long secrt = secret;
		secrt ^= secrt << 6;
		secrt %= PRUNE_CONSTANT;

		secrt ^= secrt >> 5;
		secrt %= PRUNE_CONSTANT;

		secrt ^= secrt << 11;
		secrt %= PRUNE_CONSTANT;


		return secrt;
	}

	public void part1() {
		long secretSum = 0;;

		for (long initSecrent : data) {
			long secret = initSecrent;
			for (int i = 0; i < 2000; i++) {
				secret = calculateNextSecret(secret);
			}
			secretSum += secret;
		}

		Logger.log("Sum of final secret numbers: %d", secretSum);
	}

	private int unpackChangeAtIdx(int change, int idx) {
		int firstChange = change & (0x1F << (idx*5)) >> (idx*5);
		firstChange = firstChange > 15 ? -1*(firstChange & 0xF) : firstChange;
		return firstChange;
	}

	private void debugChange(int change) {
		int firstChange = unpackChangeAtIdx(change, 3);
		int secChange = unpackChangeAtIdx(change, 2);
		int thriChange = unpackChangeAtIdx(change, 1);
		int foruChange = unpackChangeAtIdx(change, 0);
		Logger.debug("Changes: %d %d %d %d", firstChange, secChange, thriChange, foruChange);
	}

	public void part2() {
		HashMap<List<Byte>, List<Byte>> sequenceMap = new HashMap<>();

		for (long intiSecret : data) {
			HashSet<List<Byte>> addedChanges = new HashSet<>();
			ArrayList<Integer> priceQueue = new ArrayList<>();
			long secret = intiSecret;
			priceQueue.add((int)(secret % 10)); // Initial price

			for (int i = 0; i < 2000; i++) {
				secret = calculateNextSecret(secret);
				priceQueue.add((int)(secret % 10));
				if (priceQueue.size() > 5) priceQueue.removeFirst();
				if (priceQueue.size() == 5) {
					List<Byte> changes = new ArrayList<>();
					for (int j = 1; j < 5; j++) {
						changes.add((byte)(priceQueue.get(j)-priceQueue.get(j-1)));
					}
					byte priceAtChange = (byte)(priceQueue.get(4) & 0xF);

					if (!addedChanges.contains(changes)) {
						sequenceMap.putIfAbsent(changes, new ArrayList<>());
						sequenceMap.get(changes).add(priceAtChange);
						addedChanges.add(changes);
					}
				}
			}
		}

		long mostBananasGain = -1;

		for (List<Byte> key : sequenceMap.keySet()) {
			List<Byte> prices = sequenceMap.get(key);
			long priceTotal = 0;
			for (int i = 0; i < prices.size(); i++) {
				//Logger.debug(prices.get(i));
				priceTotal += prices.get(i);
			}
			mostBananasGain = Math.max(mostBananasGain, priceTotal);
			//Logger.debug("-----");
		}

		Logger.log("Most bananas that can get: %d", mostBananasGain);
	}
}
